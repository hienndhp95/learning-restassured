package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class IssueFields {
    private Fields fields;

    public IssueFields(Fields fields) {
        this.fields = fields;
    }
@Getter
    public static class Fields{
        private Project project;
        private IssueType issuetype;
        private String summary;

        public Fields(Project project, IssueType issueType, String summary) {
            this.project = project;
            this.issuetype = issueType;
            this.summary = summary;
        }
    }
@Getter
    public static class Project{
        private String key;

        public Project(String key) {
            this.key = key;
        }

    }
@Getter
    public static class IssueType{
        private String id;

        public IssueType(String id) {
            this.id = id;
        }

    }
}
