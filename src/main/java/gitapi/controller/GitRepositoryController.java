package gitapi.controller;

import gitapi.model.GitRepository;
import gitapi.service.GitRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

@Controller
public class GitRepositoryController {

    private GitRepositoryService service;

    @Autowired
    public GitRepositoryController(GitRepositoryService service) {
        this.service = service;
    }

    @RequestMapping("/repositories/{owner}")
    public ResponseEntity<List<GitRepository>> getRepositoriesAscending(
            @PathVariable String owner,
            @RequestParam(value = "sort") List<String> requestParams) throws IOException {
        return new ResponseEntity<>(service.getSortedGitRepositoryList(requestParams, owner), HttpStatus.OK);
    }
}
