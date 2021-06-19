package MLP.MLPAPI.api.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import IA.AplicacaoExcepition;
import IA.Protocolos;
import MLP.MLPAPI.api.model.AtualizaDados;



@RestController
@RequestMapping("/api")
public class MLPController {
    
    private static final Protocolos mlp = new Protocolos();
    
    
    @GetMapping("/{funcao}")
    public ResponseEntity<AtualizaDados> interage(@PathVariable String funcao) {
	switch (funcao) {
	case "treinamento":
	    mlp.treinaMLP();
	    break;
	case "treinamento1":
	    mlp.treinaUmaEpoca();
	    break;
	case "getPesos":
	    
	    break;
	case "pesosPerfeitosFinais":
	    if(!mlp.inicializaPesos(2))
		return ResponseEntity.notFound().build();
	    
	    break;
	case "PesosPerfeitosIniciais":
	    if(!mlp.inicializaPesos(1))
		return ResponseEntity.notFound().build();
	    
	    break;
	case "PesosAleatorios":
	    if(!mlp.inicializaPesos(0))
		return ResponseEntity.notFound().build();
	    
	    break;

	default:
	    return ResponseEntity.notFound().build();
	}
	
	return ResponseEntity.ok(carregaDados());
    }
    
    @GetMapping("/problema/{funcao}")
    public ResponseEntity<AtualizaDados> mudaProblema(@PathVariable String funcao) {
	if(!mlp.setMLP(funcao))
	    return ResponseEntity.notFound().build();
	
	return ResponseEntity.ok(carregaDados());
	
    }
    
    @PostMapping
    public String testaAplicacao(@RequestBody String entrada) {
	String saida="";
	try {
	    saida = mlp.aplicacao(entrada);
	} catch (AplicacaoExcepition e) {
	    saida = e.getMessage();
	}
	return saida;
    }
    
    private AtualizaDados carregaDados() {
	AtualizaDados rtn = new AtualizaDados();
	rtn.setNome(mlp.getProblema());
	rtn.setnCamadas(mlp.getNCamadas());
	rtn.setnEstados(mlp.getNEstados());
	rtn.setPesos(mlp.getPesos());
	rtn.setDadosDoGrafico(mlp.getDadosDoGrafico());
	rtn.setMatrizDeConfusao(mlp.getMatrizConfusao());
	return rtn;
       }
       

}
