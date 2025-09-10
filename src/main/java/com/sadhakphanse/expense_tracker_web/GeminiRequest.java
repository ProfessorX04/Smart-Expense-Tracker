package com.sadhakphanse.expense_tracker_web;

import java.util.List;


public class GeminiRequest {

    private List<Content> contents;

    public GeminiRequest(String prompt) {
        Part part = new Part(prompt);
        Content content = new Content(List.of(part));
        this.contents = List.of(content);
    }

    
    public List<Content> getContents() {
        return contents;
    }

    

    public static class Content {
        private List<Part> parts;

        public Content(List<Part> parts) {
            this.parts = parts;
        }

        public List<Part> getParts() {
            return parts;
        }
    }

    public static class Part {
        private String text;

        public Part(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}