package se.ecutb.data;

import org.springframework.stereotype.Component;

@Component
public class IdSequencerImpl implements IdSequencers {

    private static int personIdCounter;
    private static int todoIdCounter;

    @Override
    public int nextPersonId() {
        return ++personIdCounter;
    }

    @Override
    public int nextTodoId() {
        return ++todoIdCounter;
    }

    @Override
    public void clearPersonId() {
        personIdCounter = 0;
    }

    @Override
    public void clearTodoId() {
        todoIdCounter = 0;
    }
}
