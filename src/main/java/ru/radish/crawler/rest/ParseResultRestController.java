package ru.radish.crawler.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;
import ru.radish.crawler.database.model.ParseResult;
import ru.radish.crawler.database.service.ParseResultService;

import javax.validation.Valid;
import java.util.List;

public class ParseResultRestController {
    @Autowired
    private ParseResultService parseResultService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParseResult> getParseResult(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ParseResult parseResult = parseResultService.findOne(id);
        if (parseResult == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(parseResult, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParseResult> saveParseResult(@RequestBody @Valid ParseResult parseResult) {
        if (parseResult == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        this.parseResultService.save(parseResult);
        return new ResponseEntity<>(parseResult, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParseResult> updateParseResult(@RequestBody @Valid ParseResult parseResult, UriComponentsBuilder builder) {
        if (parseResult == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        this.parseResultService.save(parseResult);
        return new ResponseEntity<>(parseResult, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParseResult> deleteParseResult(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ParseResult parseResult = parseResultService.findOne(id);
        if (parseResult == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.parseResultService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ParseResult>> getAllParseResults() {
        List<ParseResult> parseResults = this.parseResultService.getAll();
        if (parseResults.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(parseResults, HttpStatus.OK);
    }
}
