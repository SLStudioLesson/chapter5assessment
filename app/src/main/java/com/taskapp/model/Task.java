package com.taskapp.model;

public class Task {
    private int code;
    private String name;
    private int status;
    private User repUser;

    public Task(int code, String name, int status, User repUser) {
        this.code = code;
        this.name = name;
        this.status = status;
        this.repUser = repUser;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public int getStatus() {
        return this.status;
    }

    public User getRepUser() {
        return this.repUser;
    }

    public void setStatus(int status) {
        this.status = status;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (code != other.code)
			return false;
		return true;
	}

    
}