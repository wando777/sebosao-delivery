package br.com.cwi.reset.tcc.dominio;

public enum FormaPagamento {

    CARTAO_CREDITO("Cartão de Crédito"),
    CARTAO_DEBITO("Cartão de Débito"),
    VALE_REFEICAO("Vale Refeição"),
    PIX("Pix"),
    DINHEIRO("Dinheiro");

<<<<<<< HEAD
    private String descricao;
=======
    @SuppressWarnings("unused")
	private String descricao;
>>>>>>> be1fbd80c85c2df033be65787243fdc40bb2b70b

    FormaPagamento(String descricao) {
        this.descricao = descricao;
    }
}
