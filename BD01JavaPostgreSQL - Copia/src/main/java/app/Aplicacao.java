package app;

import java.util.List;
import java.util.Scanner;

import dao.DAO;
import dao.UsuarioDAO;
import model.Usuario;

public class Aplicacao {
	
	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		
		while (true) {
            System.out.println("Menu:");
            System.out.println("1. Listar");
            System.out.println("2. Inserir");
            System.out.println("3. Excluir");
            System.out.println("4. Atualizar");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();  // Consumir a quebra de linha

            switch (opcao) {
                case 1:
                    // Listar
                    List<Usuario> usuarios = usuarioDAO.get();
                    for (Usuario u : usuarios) {
                        System.out.println(u.toString());
                    }
                    break;
                case 2:
                    // Inserir
                    Usuario novoUsuario = new Usuario();
                    System.out.print("Digite o código: ");
                    novoUsuario.setCodigo(scanner.nextInt());
                    scanner.nextLine();  // Consumir a quebra de linha
                    System.out.print("Digite o login: ");
                    novoUsuario.setLogin(scanner.nextLine());
                    System.out.print("Digite a senha: ");
                    novoUsuario.setSenha(scanner.nextLine());
                    System.out.print("Digite o sexo (M/F): ");
                    novoUsuario.setSexo(scanner.nextLine().charAt(0));
                    if (usuarioDAO.insert(novoUsuario)) {
                        System.out.println("Usuário inserido com sucesso!");
                    }
                    break;
                case 3:
                    // Excluir
                    System.out.print("Digite o código do usuário a ser excluído: ");
                    int codigoExclusao = scanner.nextInt();
                    if (usuarioDAO.delete(codigoExclusao)) {
                        System.out.println("Usuário excluído com sucesso!");
                    }
                    break;
                case 4:
                    // Atualizar
                    System.out.print("Digite o código do usuário a ser atualizado: ");
                    int codigoAtualizacao = scanner.nextInt();
                    scanner.nextLine();  // Consumir a quebra de linha
                    Usuario usuarioAtualizado = usuarioDAO.get(codigoAtualizacao);
                    if (usuarioAtualizado != null) {
                        System.out.print("Digite o novo login: ");
                        usuarioAtualizado.setLogin(scanner.nextLine());
                        System.out.print("Digite a nova senha: ");
                        usuarioAtualizado.setSenha(scanner.nextLine());
                        System.out.print("Digite o novo sexo (M/F): ");
                        usuarioAtualizado.setSexo(scanner.nextLine().charAt(0));
                        if (usuarioDAO.update(usuarioAtualizado)) {
                            System.out.println("Usuário atualizado com sucesso!");
                        }
                    } else {
                        System.out.println("Usuário não encontrado.");
                    }
                    break;
                case 5:
                    System.out.println("Saindo...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Opção inválida");
            }
        }
		
	}
}