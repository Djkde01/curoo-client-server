package com.curootest.clientback.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.curootest.clientback.domain.ClientDTO;
import com.curootest.clientback.domain.service.ClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/clients")
@Tag(name = "Client Management", description = "Operations for managing clients")
@SecurityRequirement(name = "Bearer Authentication")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Operation(summary = "Get all clients", description = "Retrieves a list of all registered clients in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of clients", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions", content = @Content)
    })
    @GetMapping("/all")
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        return new ResponseEntity<>(clientService.getAllClients(), HttpStatus.OK);
    }

    @Operation(summary = "Get client by ID type and number", description = "Retrieves a specific client using their identification type and number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client found successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Client not found with the specified ID type and number", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required", content = @Content)
    })
    @GetMapping("/{idType}/{idNumber}")
    public ResponseEntity<ClientDTO> getClientByIdNumber(
            @Parameter(description = "Type of identification (e.g., CC, TI, CE)", required = true, example = "CC") @PathVariable String idType,
            @Parameter(description = "Identification number", required = true, example = "1234567890") @PathVariable String idNumber) {
        return clientService.getClientByIdNumber(idType, idNumber)
                .map(client -> new ResponseEntity<>(client, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Create a new client", description = "Creates a new client record in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation errors", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict - Client with this ID already exists", content = @Content)
    })
    @PostMapping("/save")
    public ResponseEntity<ClientDTO> saveClient(
            @Parameter(description = "Client data to be created", required = true) @RequestBody ClientDTO clientDTO) {
        return new ResponseEntity<>(clientService.saveClient(clientDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a client", description = "Removes a client from the system by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Client not found with the specified ID", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required", content = @Content)
    })
    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable Integer clientId) {
        return clientService.deleteClient(clientId)
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
