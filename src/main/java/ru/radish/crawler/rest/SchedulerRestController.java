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
import ru.radish.crawler.database.model.Scheduler;
import ru.radish.crawler.database.service.SchedulerService;

import javax.validation.Valid;
import java.util.List;

public class SchedulerRestController {
    @Autowired
    private SchedulerService schedulerService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Scheduler> getScheduler(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Scheduler scheduler = schedulerService.findOne(id);
        if (scheduler == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(scheduler, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Scheduler> saveScheduler(@RequestBody @Valid Scheduler scheduler) {
        if (scheduler == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        this.schedulerService.save(scheduler);
        return new ResponseEntity<>(scheduler, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Scheduler> updateScheduler(@RequestBody @Valid Scheduler scheduler, UriComponentsBuilder builder) {
        if (scheduler == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        this.schedulerService.save(scheduler);
        return new ResponseEntity<>(scheduler, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Scheduler> deleteScheduler(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Scheduler scheduler = schedulerService.findOne(id);
        if (scheduler == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.schedulerService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Scheduler>> getAllSchedulers() {
        List<Scheduler> schedulers = this.schedulerService.getAll();
        if (schedulers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(schedulers, HttpStatus.OK);
    }
}
