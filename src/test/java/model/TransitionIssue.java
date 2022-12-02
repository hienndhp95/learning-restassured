package model;

import lombok.Getter;

public class TransitionIssue {
    private TransitionId transition;

    public TransitionIssue(TransitionId transitionId) {
        this.transition = transitionId;
    }

    @Getter
    public static class TransitionId{
        private String id;

        public TransitionId(String transitionId) {
            this.id = transitionId;
        }
    }


}
