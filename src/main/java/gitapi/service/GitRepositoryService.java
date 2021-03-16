package gitapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gitapi.model.GitRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GitRepositoryService {

    public List<GitRepository> getSortedGitRepositoryList(List<String> requestParams, String owner) throws IOException {
        Comparator<GitRepository> compareByStars = Comparator.comparing(GitRepository::getStars);
        List<GitRepository> gitRepositoryList = getGitRepositoryList(owner);
        if (requestParams.contains("asc")) {
            gitRepositoryList.sort(compareByStars);
        } else if (requestParams.contains("desc")) {
            gitRepositoryList.sort(compareByStars.reversed());
        }
        return gitRepositoryList;
    }

    protected List<GitRepository> getGitRepositoryList(String owner) throws IOException {
        List<GitRepository> gitRepositoryList = new ArrayList<>();
        for (List<String> project : getProjects(owner)) {
            List<String> gitRepository = new ArrayList<>();
            for (String line : project) {
                if (line.contains("html_url")) {
                    StringBuilder stringBuilder = new StringBuilder(line);
                    StringBuilder reversed = new StringBuilder(stringBuilder.reverse().substring(0, stringBuilder.indexOf("/")));
                    gitRepository.add("\"full_name\":\"" + reversed.reverse().toString());
                }
                if (line.matches(".*(html_url|description|stargazers_count|created_at).*")) {
                    gitRepository.add(line);
                }
            }
            gitRepositoryList.add(new ObjectMapper().readValue("{" + String.join(", ", gitRepository) + "}", GitRepository.class));
        }
        return gitRepositoryList;
    }

    protected List<List<String>> getProjects(String owner) throws IOException {
        String json = readJsonFromUrl("https://api.github.com/users/" + owner + "/repos");
        return Arrays.stream(json.substring(1, json.length() - 1)
                .split("},\\{"))
                .map(project -> Arrays.asList(project.substring(project.indexOf("},") + 2).split(",")))
                .collect(Collectors.toList());
    }

    private String readJsonFromUrl(String url) throws IOException {
        try (InputStream is = new URL(url).openStream()) {
            return readAll(new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8"))));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Podany użtykownik nie istnieje lub nie ma publicznych repozytoriów.");
        }
    }
    protected String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.substring(1, sb.length() - 1);
    }
}
