package org.sid.maBanque.web;

import org.sid.maBanque.dao.ClientRepository;
import org.sid.maBanque.dao.CompteRepository;
import org.sid.maBanque.entities.AccountType;
import org.sid.maBanque.entities.Client;
import org.sid.maBanque.entities.Compte;
import org.sid.maBanque.entities.Operation;
import org.sid.maBanque.metier.IBanqueMetier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BanqueController {

	@Autowired
	private IBanqueMetier banqueMetier;

	@Autowired
	private CompteRepository compteRepository;

	@Autowired
	private ClientRepository clientRepository;

	/**
	 * Redirect to appropriate operations page depending on user role
	 */
	@RequestMapping("/operations")
	public String redirectBasedOnRole() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			for (GrantedAuthority authority : authentication.getAuthorities()) {
				String role = authority.getAuthority();

				if (role.equals("ADMIN")) {
					return "redirect:/admin/operations";
				} else if (role.equals("USER")) {
					return "redirect:/user/operations";
				}
			}
		}

		// fallback if no role matched
		return "redirect:/login?error=unauthorized";
	}

	@RequestMapping("/user/operations")
	public String userOperations(Model model,
								 @RequestParam(name = "page", defaultValue = "0") int page,
								 @RequestParam(name = "size", defaultValue = "5") int size) {
		String codeCompte = "C1"; // Hardcoded for demo/testing
		model.addAttribute("codeCompte", codeCompte);
		try {
			Compte cp = banqueMetier.consulterCompte(codeCompte);
			Page<Operation> pageOperations = banqueMetier.listOperation(codeCompte, page, size);
			model.addAttribute("listOperations", pageOperations.getContent());

			int[] pages = new int[pageOperations.getTotalPages()];
			model.addAttribute("pages", pages);
			model.addAttribute("compte", cp);
		} catch (Exception e) {
			model.addAttribute("exception", e);
		}
		return "comptes";
	}


	/**
	 * Operations view for admin
	 */
	@RequestMapping("/admin/operations")
	public String adminOperations() {
		return "adminOperations"; // Create adminOperations.html in templates
	}


	/**
	 * Account consultation logic
	 */
	@RequestMapping("/user/consulterCompte")
	public String consulter(Model model,
							String codeCompte,
							@RequestParam(name = "page", defaultValue = "0") int page,
							@RequestParam(name = "size", defaultValue = "5") int size) {
		model.addAttribute("codeCompte", codeCompte);
		try {
			Compte cp = banqueMetier.consulterCompte(codeCompte);
			Page<Operation> pageOperations = banqueMetier.listOperation(codeCompte, page, size);
			model.addAttribute("listOperations", pageOperations.getContent());

			int[] pages = new int[pageOperations.getTotalPages()];
			model.addAttribute("pages", pages);
			model.addAttribute("compte", cp);
		} catch (Exception e) {
			model.addAttribute("exception", e);
		}
		return "comptes";
	}

	/**
	 * Account consultation logic
	 */
	@RequestMapping("/admin/consulterCompte")
	public String consulterAdmin(Model model,
							String codeCompte,
							@RequestParam(name = "page", defaultValue = "0") int page,
							@RequestParam(name = "size", defaultValue = "5") int size) {
		model.addAttribute("codeCompte", codeCompte);
		try {
			Compte cp = banqueMetier.consulterCompte(codeCompte);
			Page<Operation> pageOperations = banqueMetier.listOperation(codeCompte, page, size);
			model.addAttribute("listOperations", pageOperations.getContent());

			int[] pages = new int[pageOperations.getTotalPages()];
			model.addAttribute("pages", pages);
			model.addAttribute("compte", cp);
		} catch (Exception e) {
			model.addAttribute("exception", e);
		}
		return "adminComptes";
	}

	/**
	 * Save banking operation
	 */
	@RequestMapping(value = "/admin/saveOperation", method = RequestMethod.POST)
	public String saveOperation(Model model,
								String typeOperation,
								String codeCompte,
								double montant,
								String codeCompte2) {
		try {
			switch (typeOperation) {
				case "VERS":
					banqueMetier.verser(codeCompte, montant);
					break;
				case "RETR":
					banqueMetier.retirer(codeCompte, montant);
					break;
				case "VIR":
					banqueMetier.virement(codeCompte, codeCompte2, montant);
					break;
			}
		} catch (Exception e) {
			model.addAttribute("error", e);
			return "redirect:/admin/consulterCompte?codeCompte=" + codeCompte + "&error=" + e.getMessage();
		}
		return "redirect:/admin/consulterCompte?codeCompte=" + codeCompte;
	}

	/**
	 * Save banking operation
	 */
	@RequestMapping(value = "/user/saveOperation", method = RequestMethod.POST)
	public String saveOperationUser(Model model,
								String typeOperation,
								String codeCompte,
								double montant,
								String codeCompte2) {
		try {
			switch (typeOperation) {
				case "VERS":
					banqueMetier.verser(codeCompte, montant);
					break;
				case "RETR":
					banqueMetier.retirer(codeCompte, montant);
					break;
				case "VIR":
					banqueMetier.virement(codeCompte, codeCompte2, montant);
					break;
			}
		} catch (Exception e) {
			model.addAttribute("error", e);
			return "redirect:/user/consulterCompte?codeCompte=" +  "C1" + "&error=" + e.getMessage();
		}
		return "redirect:/user/consulterCompte?codeCompte=" + "C1";
	}
	@GetMapping("/admin/clientsList")
	public String getClients(Model model) {
		List<Client> clients = clientRepository.findAll();
		model.addAttribute("clients", clients);
		return "clientsList";
	}

	@GetMapping("/admin/comptesList")
	public String getComptes(Model model) {
		List<Compte> comptes = compteRepository.findAll();
		model.addAttribute("comptes", comptes);
		return "comptesList";
	}


	@Controller
	public class OperationRedirectController {

		@GetMapping("/operations")
		public String redirectBasedOnRole(Authentication authentication) {
			if (authentication != null && authentication.getAuthorities() != null) {
				boolean isAdmin = authentication.getAuthorities().stream()
						.anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
				if (isAdmin) {
					return "redirect:/admin/operations";
				}
			}
			return "redirect:/user/operations";
		}
	}

//	/**
//	 * Create Account
//	 */
//	@RequestMapping(value = "/admin/createAccount", method = RequestMethod.POST)
//	public String createAccount(Model model, Long clientId, String accountType, double initialBalance) {
//		try {
//			AccountType type = AccountType.valueOf(accountType);
//			Compte newAccount = banqueMetier.createAccount(clientId, type, initialBalance);
//			model.addAttribute("successMessage", "Account created successfully: " + newAccount.getCodeCompte());
//			return "redirect:/user/consulterCompte?codeCompte=" + newAccount.getCodeCompte();
//		} catch (Exception e) {
//			model.addAttribute("error", e.getMessage());
//			return "error";
//		}
//	}
}
