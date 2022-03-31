package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.AttachmentDTO;
import com.example.springbootdemo.dto.BugDTO;
import com.example.springbootdemo.model.enums.BugStatus;
import com.example.springbootdemo.service.AttachmentService;
import com.example.springbootdemo.service.BugService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", originPatterns = "*", allowedHeaders = "*")
public class BugController {
    @Autowired
    private BugService bugService;
    @Autowired
    private AttachmentService attachmentService;

    @GetMapping("/bugs")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<BugDTO>> findAll() {
        return new ResponseEntity<>(bugService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/bugs/attachmentList/{id}")
    public ResponseEntity<List<AttachmentDTO>> findAttachmentsForBug(@PathVariable("id") Long id) {
        return new ResponseEntity<>(attachmentService.findByBug(id), HttpStatus.OK);
    }

    @PostMapping("/bugs/add/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BugDTO> addBug(@RequestBody BugDTO bugDTO, @PathVariable("username") String username) throws ValidationException {
        System.out.println(bugDTO);
        return new ResponseEntity<>(bugService.addBug(bugDTO, username), HttpStatus.OK);
    }

    @PostMapping("/bugs/addAttachment/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> addAttachment(@RequestParam("file") MultipartFile file, @PathVariable("id") Long id) throws IOException {
        return new ResponseEntity<>(attachmentService.addAttachmentToBug(file, id), HttpStatus.OK);
    }

    @DeleteMapping("/bugs/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> deleteBugById(@PathVariable Long id) {
        return new ResponseEntity<>(bugService.removeBug(id), HttpStatus.OK);
    }

    @GetMapping("/bugs/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BugDTO> findById(@PathVariable Long id) {
        return new ResponseEntity<>(bugService.findBugById(id), HttpStatus.OK);
    }

    @GetMapping("/findBugsByTitle/{title}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BugDTO> findByTitle(@PathVariable String title) {
        return new ResponseEntity<>(bugService.findBugByTitle(title), HttpStatus.OK);
    }

    @PutMapping("/bugs/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BugDTO> updateById(@PathVariable("id") Long id, @RequestBody BugDTO bug) throws ValidationException {
        return new ResponseEntity<>(bugService.updateById(id, bug), HttpStatus.OK);
    }

    @GetMapping("/bugs/assignedto/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<BugDTO>> findBugsAssignedTo(@PathVariable("username") String username) {
        return new ResponseEntity<>(bugService.findBugsAssignedTo(username), HttpStatus.OK);
    }

    @GetMapping("/bugs/createdby/{username}")
    public ResponseEntity<List<BugDTO>> findBugsCreatedBy(@PathVariable("username") String username) {
        return new ResponseEntity<>(bugService.findBugsCreatedBy(username), HttpStatus.OK);
    }

    @GetMapping("/bugs/attachment/{id}")
    public ResponseEntity<byte[]> findAttachmentById(@PathVariable Long id) {
        try {
            AttachmentDTO fileDB = attachmentService.findById(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getTitle() + "\"")
                    .body(fileDB.getContent());
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/bugs/statistics/")
    public ResponseEntity<EnumMap<BugStatus, Long>> getStatistics() {
       return new ResponseEntity<>(bugService.generateStatistics(), HttpStatus.OK);
    }

    @GetMapping("/bugs/statistics/rejected")
    public ResponseEntity<Map<String, Long>> getCreatedAndRejected() {
        return new ResponseEntity<>(bugService.generateStatisticsCreatedRejected(), HttpStatus.OK);
    }

    @GetMapping("/bugs/statistics/byUser/{id}")
    public ResponseEntity<Map<String, Long>> getStatisticsForUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(bugService.generateStatisticsForUser(id), HttpStatus.OK);
    }

}
