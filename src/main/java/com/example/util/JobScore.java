package com.example.util;

import com.example.entity.JobPost;

public class JobScore {

    private JobPost job;
    private int score;

    public JobScore(JobPost job, int score) {
        this.job = job;
        this.score = score;
    }

    public JobPost getJob() {
        return job;
    }

    public int getScore() {
        return score;
    }
}