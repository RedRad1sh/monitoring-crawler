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
import ru.radish.crawler.database.model.ParsePlan;
import ru.radish.crawler.database.service.ParsePlanService;

import javax.validation.Valid;
import java.util.List;

public class ParsePlanRestController {
    @Autowired
    private ParsePlanService parsePlanService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParsePlan> getParsePlan(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ParsePlan parsePlan = parsePlanService.findOne(id);
        if (parsePlan == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(parsePlan, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParsePlan> saveParsePlan(@RequestBody @Valid ParsePlan parsePlan) {
        if (parsePlan == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        this.parsePlanService.save(parsePlan);
        return new ResponseEntity<>(parsePlan, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParsePlan> updateParsePlan(@RequestBody @Valid ParsePlan parsePlan, UriComponentsBuilder builder) {
        if (parsePlan == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        this.parsePlanService.save(parsePlan);
        return new ResponseEntity<>(parsePlan, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParsePlan> deleteParsePlan(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ParsePlan parsePlan = parsePlanService.findOne(id);
        if (parsePlan == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.parsePlanService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ParsePlan>> getAllParsePlans() {
        List<ParsePlan> parsePlans = this.parsePlanService.getAll();
        if (parsePlans.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(parsePlans, HttpStatus.OK);
    }
}
