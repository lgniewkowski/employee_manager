package com.lgniewkowski.em.exceptions;

public class CyclicInHierarchyException extends RuntimeException {
    private static final String MESSAGE = "Trying to add supervisor with %d id, to employee with %d id";

    public CyclicInHierarchyException(Long id, Long supervisorId) {
        super(String.format(MESSAGE, supervisorId, id));
    }
}
