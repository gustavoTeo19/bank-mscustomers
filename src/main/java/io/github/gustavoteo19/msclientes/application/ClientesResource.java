package io.github.gustavoteo19.msclientes.application;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import io.github.gustavoteo19.msclientes.application.representation.ClienteSaveRequest;
import io.github.gustavoteo19.msclientes.domain.Cliente;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("clientes")
@Slf4j
public class ClientesResource {
    private final ClienteService clienteService;

    public ClientesResource(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String status(){
        log.info("Obtendo o status do microsservice de clientes");
        return "ok";
    }

    @PostMapping
    public ResponseEntity save(@RequestBody ClienteSaveRequest request){
        Cliente cliente = request.toModel();
        clienteService.save(cliente);
        URI headerLocation = ServletUriComponentsBuilder.fromCurrentRequest().query("cpf={cpf}")
                            .buildAndExpand(cliente.getCpf()).toUri();
        return ResponseEntity.created(headerLocation).build();
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<Object> getdadosCliente(@RequestParam("cpf") String cpf){
        Optional<Cliente> cliente  = clienteService.getByCPF(cpf);
        if(cliente.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }
}
