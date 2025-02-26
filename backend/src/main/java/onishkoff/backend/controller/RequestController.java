package onishkoff.backend.controller;

import lombok.RequiredArgsConstructor;
import onishkoff.backend.dto.model.request.RequestDto;
import onishkoff.backend.model.enums.Status;
import onishkoff.backend.service.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<Void> createRequest(@RequestBody RequestDto request){
        requestService.createRequest(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/teacher")
    public ResponseEntity<List<RequestDto>> getAllTeacherRequest(@RequestParam(name = "organization") Long organizationId){
        return ResponseEntity.ok(requestService.getAllTeacherRequests(organizationId));
    }

    @GetMapping("/student")
    public ResponseEntity<List<RequestDto>> getAllStudentRequest(@RequestParam(name = "organization") Long organizationId){
        return ResponseEntity.ok(requestService.getAllStudentRequests(organizationId));
    }

    @GetMapping("/admin")
    public ResponseEntity<List<RequestDto>> getAllAdminRequest(@RequestParam(name = "organization") Long organizationId){
        return ResponseEntity.ok(requestService.getAllAdminRequests(organizationId));
    }

    @PutMapping("/handleRequest/{id}")
    public ResponseEntity<Void> handleRequest(@PathVariable Long id, @RequestParam(name = "status") Status status){
        requestService.handleRequest(id, status);
        return ResponseEntity.ok().build();
    }


}
