$(document).ready(function() {
	alert('entrou');
	var groups=document.indexForm.Combo01.options.length;
	var group=new Array(groups);
	var i;
	for (i=0; i<groups; i++){
		group[i]=new Array();
	}

	group[0][0]=new Option("Bairro");

	group[1][0]=new Option("Areias");
	group[1][1]=new Option("Barreiros");
	group[1][2]=new Option("Bela Vista");
	group[1][3]=new Option("Bosque das Mansões");
	group[1][4]=new Option("Campinas");
	group[1][5]=new Option("Centro Histórico");
	group[1][6]=new Option("Colônia Santana");
	group[1][7]=new Option("Distrito Industrial");
	group[1][8]=new Option("Fazenda do Max");
	group[1][9]=new Option("Fazenda Sto. Antônio");
	group[1][10]=new Option("Flor de Nápolis");
	group[1][11]=new Option("Forquilhas");
	group[1][12]=new Option("Forquilinhas");
	group[1][13]=new Option("Ipiranga");
	group[1][14]=new Option("Jardim Cidade de Florianópolis");
	group[1][15]=new Option("Jardim Santiago");
	group[1][16]=new Option("Kobrasol");
	group[1][17]=new Option("N. Sra. do Rosário");
	group[1][18]=new Option("Pedregal");
	group[1][19]=new Option("Picadas do Sul");
	group[1][20]=new Option("Potecas");
	group[1][21]=new Option("Praia Comprida");
	group[1][22]=new Option("Real Parque");
	group[1][23]=new Option("Roçado");
	group[1][24]=new Option("São Luiz");
	group[1][25]=new Option("Serraria");
	group[1][26]=new Option("Sertão do Maruim");

	group[2][0]=new Option("Alto Aririú");
	group[2][1]=new Option("Aririú");
	group[2][2]=new Option("Aririú da Formiga");
	group[2][3]=new Option("Barra do Aririú");
	group[2][4]=new Option("Bela Vista");
	group[2][5]=new Option("Brejaru");
	group[2][6]=new Option("Caminho Novo");
	group[2][7]=new Option("Centro");
	group[2][8]=new Option("Jardim Aquarius");
	group[2][9]=new Option("Jardim Eldorado");
	group[2][10]=new Option("Madrid");
	group[2][11]=new Option("Morretes");
	group[2][12]=new Option("Pachecos");
	group[2][13]=new Option("Pagani");
	group[2][14]=new Option("Pagará");
	group[2][15]=new Option("Passa Vinte");
	group[2][16]=new Option("Pedra Branca");
	group[2][17]=new Option("Ponta de Baixo");
	group[2][18]=new Option("Ponte do Imaruim");
	group[2][19]=new Option("Rio Grande");
	group[2][20]=new Option("São Sebastião");


	var temp=document.indexForm.Combo02;

	function redirect(x){
		alert('entrou');
	for (i=0;i<group[x].length;i++){
	temp.options[i]=new Option(group[x][i].text,group[x][i].value);
	}
	temp.options[0].selected=true
	}
});