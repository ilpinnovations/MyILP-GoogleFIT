package com.ilp.ilpschedule.model;

import java.io.Serializable;

/**
 * Created by kaustav on 11/27/2015.
 */
public class Questions implements Serializable {
    int emp_id;
    int ques_id;
    String status;
    String option1,option2,option3,option4,question;
    String submit_time;
    String emp_name,emp_loc;
    int views;
    int correct_ans;

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getEmp_loc() {
        return emp_loc;
    }

    public void setEmp_loc(String emp_loc) {
        this.emp_loc = emp_loc;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public Questions(int emp_id, int ques_id, String status, String option1, String option2, String option3, String option4, String question, String submit_time, int correct_ans,String emp_name,String emp_loc,int views) {
        this.emp_id = emp_id;
        this.ques_id = ques_id;
        this.status = status;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.question = question;
        this.submit_time = submit_time;
        this.correct_ans = correct_ans;
        this.views=views;
        this.emp_name=emp_name;
        this.emp_loc=emp_loc;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public int getQues_id() {
        return ques_id;
    }

    public void setQues_id(int ques_id) {
        this.ques_id = ques_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSubmit_time() {
        return submit_time;
    }

    public void setSubmit_time(String submit_time) {
        this.submit_time = submit_time;
    }

    public int getCorrect_ans() {
        return correct_ans;
    }

    public void setCorrect_ans(int correct_ans) {
        this.correct_ans = correct_ans;
    }
}
