package com.bestbuy.oms.marketplace.workers;

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import com.netflix.conductor.common.metadata.tasks.TaskResult.Status;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FraudCheckWorker implements Worker {

    private final String taskDefName = "fraud";

    @Override
    public String getTaskDefName() {
        return taskDefName;
    }

    @Override
    public TaskResult execute(Task task) {
        TaskResult result = new TaskResult(task);
        result.setStatus(Status.COMPLETED);

        //Register the output of the task
        result.getOutputData().put("result", "Fraud Check was successful");


        return result;
    }
}