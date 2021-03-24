package br.com.cwi.reset.tcc.dominio;

<<<<<<< HEAD
import javax.persistence.*;
import java.util.List;

=======
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

>>>>>>> be1fbd80c85c2df033be65787243fdc40bb2b70b
@Entity
public class Estabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

<<<<<<< HEAD
    private String cnpj;

    private String nomeFantasia;

=======
    @NotBlank
    private String cnpj;

    @NotBlank
    private String nomeFantasia;

    @NotBlank
>>>>>>> be1fbd80c85c2df033be65787243fdc40bb2b70b
    private String razaoSocial;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_estabelecimento")
<<<<<<< HEAD
=======
    @Valid
>>>>>>> be1fbd80c85c2df033be65787243fdc40bb2b70b
    private List<HorarioFuncionamento> horariosFuncionamento;

    @ElementCollection(targetClass = FormaPagamento.class)
    @JoinTable(name = "FORMAS_PAGAMENTO", joinColumns = @JoinColumn(name = "cnpj"))
    @Enumerated(EnumType.STRING)
<<<<<<< HEAD
=======
    @Valid
>>>>>>> be1fbd80c85c2df033be65787243fdc40bb2b70b
    private List<FormaPagamento> formasPagamento;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_estabelecimento")
    private List<Endereco> enderecos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public List<HorarioFuncionamento> getHorariosFuncionamento() {
        return horariosFuncionamento;
    }

    public void setHorariosFuncionamento(List<HorarioFuncionamento> horariosFuncionamento) {
        this.horariosFuncionamento = horariosFuncionamento;
    }

    public List<FormaPagamento> getFormasPagamento() {
        return formasPagamento;
    }

    public void setFormasPagamento(List<FormaPagamento> formasPagamento) {
        this.formasPagamento = formasPagamento;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }
}
