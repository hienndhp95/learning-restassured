package builder;

import model.IssueTransition;

public class TransitionContentBuilder {
    private IssueTransition transitionIssue;

    public String build(String transitionId){
        IssueTransition.Transition id = new IssueTransition.Transition(transitionId);
        transitionIssue = new IssueTransition(id);
        return BodyJSONBuilder.getJSONContent(transitionIssue);
    }
}
