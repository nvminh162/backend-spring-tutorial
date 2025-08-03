package vn.hoidanit.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hoidanit.domain.Task;
import vn.hoidanit.repository.TaskRepository;
import vn.hoidanit.service.TaskService;

/**
 * Service Implementation for managing {@link vn.hoidanit.domain.Task}.
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task save(Task task) {
        LOG.debug("Request to save Task : {}", task);
        return taskRepository.save(task);
    }

    @Override
    public Task update(Task task) {
        LOG.debug("Request to update Task : {}", task);
        return taskRepository.save(task);
    }

    @Override
    public Optional<Task> partialUpdate(Task task) {
        LOG.debug("Request to partially update Task : {}", task);

        return taskRepository
            .findById(task.getId())
            .map(existingTask -> {
                if (task.getTitle() != null) {
                    existingTask.setTitle(task.getTitle());
                }
                if (task.getDescription() != null) {
                    existingTask.setDescription(task.getDescription());
                }

                return existingTask;
            })
            .map(taskRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Task> findOne(Long id) {
        LOG.debug("Request to get Task : {}", id);
        return taskRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Task : {}", id);
        taskRepository.deleteById(id);
    }
}
