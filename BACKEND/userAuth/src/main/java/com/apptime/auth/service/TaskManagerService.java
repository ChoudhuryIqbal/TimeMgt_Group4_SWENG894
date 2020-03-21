package com.apptime.auth.service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.apptime.auth.config.TaskStateMachine;
import com.apptime.auth.model.TaskState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.apptime.auth.model.Task;
import com.apptime.auth.repository.TaskRepository;

import javax.transaction.Transactional;

/**
 * @author Bashiir Mohamed
 * this class represent  task business service layer.
 */
@Service
public class TaskManagerService {
	@Autowired
	TaskRepository taskRepo;

	@Autowired
	NotificationService notificationService;

    //view task details
	//view task details
	public Task getTask(long id) {
		return taskRepo.findById(id);

	}
	public Task getTask(long id, String username) {
		return taskRepo.findByIdAndUserName(id,username);

	}
	public Task getTask(TaskState ts, String userName){
		return taskRepo.findByUserNameAndState(userName,ts);
	}
	//create task
	public Task createTask(Task task, String user) {

		task.setUserName(user);
		TaskStateMachine.CREATE(task);
		taskRepo.save(task);
		notificationService.createNotificationForTask(task);
		return task;
	}


	//update task
	public Task updateTask(@RequestBody Task task, String username) {
		Task old = taskRepo.findById(task.getId());
		if (old == null || !old.getUserName().equals(username)) {
			return null;
		}
		task.setId(old.getId());
		task.setUserName(username);
		task.setState(old.getState());
		taskRepo.save(task);
		notificationService.updateNotificationForTask(task);
		return task;
	}

	//delete task
	@Transactional
	public Task deleteTask(long id) {
		Task old = taskRepo.findById(id);
		if (old != null && old.getState() != null && !old.getState().equals(TaskState.CREATED) && !old.getState().equals(TaskState.COMPLETED)) {
			return null;
		}
		if (old != null) {
			taskRepo.delete(old);
			notificationService.deleteNotificationForTask(old);
		}
		return old;

	}
	public List<Task> findUserTasks(String user) {
		return taskRepo.findByUserName(user);
	}



	void setNotificationService(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	/**
	 *
	 * @param taskId
	 * @param startDate
	 * @return
	 */
	public TaskState start(long  taskId, Date startDate ){
		Task task = taskRepo.findById(taskId);
		TaskState ts = null;
		if(task != null){
			TaskStateMachine.START(task);
			task.setStart(startDate);
			taskRepo.save(task);
			ts = task.getState();
		}
		return ts;

	}

	/**
	 *
	 * @param taskId
	 * @return
	 */
	public TaskState pause(long  taskId){
		Task task = taskRepo.findById(taskId);
		TaskState ts = null;
		if(task != null){
			TaskStateMachine.PAUSE(task);
			taskRepo.save(task);
			ts = task.getState();
		}
		return ts;


	}



	public TaskState complete(long  taskId, Date endDate){
		Task task = taskRepo.findById(taskId);
		TaskState ts = null;
		if(task != null){
			TaskStateMachine.COMPLETE(task);
			task.setEnd(endDate);
			taskRepo.save(task);
			ts = task.getState();
		}
		return ts;

	}


    public Set<Task> getStartingTask(Date start, String name) {
		Set<Task> tasks = taskRepo.getSpecificDayTasks(start,name);
		Set<Task> result = new HashSet<Task>();
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String stDate = simpleDateFormat.format(start);
		if(tasks != null && !tasks.isEmpty()){
			for(Task task : tasks){
				SimpleDateFormat simpleD = new SimpleDateFormat(pattern);
				String schldDate = simpleD.format(task.getScheduledstart());
				if (schldDate.equals(stDate)) {
					result.add(task);
				}
				}
			}
		return result;
    }
}
