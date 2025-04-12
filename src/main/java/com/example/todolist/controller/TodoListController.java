package com.example.todolist.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoData;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.service.TodoService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TodoListController {
	private final TodoRepository todoRepository;
	private final TodoService todoService; //Todolist2で追加
	
	//ToDo一覧表示(Todolistで追加)
	@GetMapping("/todo")
	public ModelAndView showTodoList(ModelAndView mv) {
		mv.setViewName("todoList");
		List<Todo>todoList=todoRepository.findAll();
		mv.addObject("todoList",todoList);
		return mv;
	}
	
	//Todo入力フォーム表示(Todolist2で追加)
	//【処理1】Todo一覧画面(todoList.html)で[新規追加]リンクがクリックされたとき
	@GetMapping("/todo/create")
	public ModelAndView createTodo(ModelAndView mv) {
		mv.setViewName("todoForm");
		mv.addObject("todoData",new TodoData());
		return mv;
	}
	
	//ToDo追加処理(Todolist2で追加)
	//【処理2】ToDo入力画面(todoForm.html)で[登録]ボタンがクリックされたとき
	@PostMapping("/todo/create")
	public ModelAndView createTodo(@ModelAttribute @Validated TodoData todoData,
			BindingResult result, ModelAndView mv) {
	//エラーチェック
		boolean isValid=todoService.isValid(todoData,result);
		if(!result.hasErrors()&&isValid) {
			//エラーなし
			Todo todo=todoData.toEntity();
			todoRepository.saveAndFlush(todo);
			return showTodoList(mv);
		}else {
			//エラーあり
			mv.setViewName("todoForm");
			//mv.addObject("todoData", todoData);
			return mv;
		}
	}
	
	//Todo一覧へ戻る（Todolist2で追加）
	//【処理3】Todo入力画面で[キャンセル登録]ボタンがクリックされたとき
	@PostMapping("/todo/cancel")
	public String cancel() {
		return "redirect:/todo";
	}

}
