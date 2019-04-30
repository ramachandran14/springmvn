package com.ofs.maven.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ofs.maven.Impl.ToDoServiceImpl;
import com.ofs.maven.model.ToDo;

import exception.ServiceException;

@Controller
public class HomeController {
	@Autowired
	private ToDoServiceImpl todoServiceImpl;
	
	@RequestMapping(value="/todo/{var}",  method=RequestMethod.GET)
	public ModelAndView redirect(@PathVariable String var) {
		ModelAndView model = new ModelAndView(var);
		return model;
	}
	
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public ModelAndView listTodo(ModelAndView model) {
		List<ToDo> listToDo = todoServiceImpl.getAllToDos();
		model = new ModelAndView("index");
		model.addObject("listToDo", listToDo);
		return model;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(HttpServletRequest req, @RequestParam("tasks") String tasks, @RequestParam("status") String status, @RequestParam("due_date") String due_date) throws ServiceException {
		ToDo todo = new ToDo();
		todo.setTasks(tasks);
		todo.setStatus(status);
		todo.setDue_date(due_date);
		todoServiceImpl.create(todo);
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(@ModelAttribute ToDo todo) {
		todoServiceImpl.update(todo);
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ModelAndView delete(HttpServletRequest req) {
		int id = Integer.parseInt(req.getParameter("value"));
		try {
			todoServiceImpl.delete(id);
			ModelAndView model = new ModelAndView("success");
			model.setStatus(HttpStatus.OK);
			model.addObject("msg", "The Value is deleted");
		} catch (ServiceException se) {
			ModelAndView model = new ModelAndView("error");
			model.setStatus(HttpStatus.BAD_REQUEST);
			model.addObject("msg", "The Value is not present");
		} catch(Exception e) {
			ModelAndView model = new ModelAndView("error");
			model.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			model.addObject("msg", "Internal server error");
		}
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value="/read", method=RequestMethod.GET)
	public ModelAndView read(HttpServletRequest req) {
		int id = Integer.parseInt(req.getParameter("id"));
		ToDo todo = todoServiceImpl.getTodoById(id);
		ModelAndView model = new ModelAndView("ToDoForm");
		model.addObject("todo", todo);
		return model;
	}
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public ModelAndView search(HttpServletRequest req) {
		String tasks = req.getParameter("tasks");
		List<ToDo> listToDo = todoServiceImpl.searchToDo(tasks);
		ModelAndView model = new ModelAndView("index");
		model.addObject("listToDo", listToDo);
		return model;
	}
}