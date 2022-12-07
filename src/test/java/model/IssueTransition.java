package model;

import lombok.Getter;

public class IssueTransition {
    private final Transition transition;

    public IssueTransition(Transition transition) {
        this.transition = transition;
    }

    @Getter
    public static class Transition {
        private String id;

        public Transition(String transition) {
            this.id = transition;
        }
    }


}
