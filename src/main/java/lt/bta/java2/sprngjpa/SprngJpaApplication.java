package lt.bta.java2.sprngjpa;

import lt.bta.java2.sprngjpa.entities.Invoice;
import lt.bta.java2.sprngjpa.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class SprngJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SprngJpaApplication.class, args);
	}

}

@RepositoryRestResource(path = "product")
interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

	List<Product> findByNameContains(String name);

	List<Product> findByPriceLessThan(BigDecimal price);
}

@RestController
@RequestMapping("/api/product")
class ProductApi {

	@Autowired
	private ProductRepository productRepository;

	@GetMapping("/{id}")
	public ResponseEntity<Product> get(@PathVariable int id) {
		//return productRepository.findById(id).map(ResponseEntity::ok).orElseGet((() -> ResponseEntity.notFound().build()));
		Optional<Product> product = productRepository.findById(id);
		if (product.isPresent()) {
			return ResponseEntity.ok(product.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/page")
	public Page<Product> get(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sort) {
		return productRepository.findAll(PageRequest.of(page, size, Sort.by(sort)));
	}

	@PostMapping
	public Product add(@RequestBody Product product) {
		return productRepository.save(product);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		productRepository.deleteById(id);
	}

	@GetMapping("/find/name-like")
	public List<Product> findByNameContains(@RequestParam String s) {
		return productRepository.findByNameContains(s);
	}

	@GetMapping("/find/price-less")
	public List<Product> findByPriceLessThan(@RequestParam BigDecimal p) {
		return productRepository.findByPriceLessThan(p);
	}
}


//
// Invoices
//

@RepositoryRestResource(path = "invoice")
interface InvoiceRepository extends  PagingAndSortingRepository<Invoice, Integer> {}

@RestController
@RequestMapping("/api/invoice")
class InvoiceApi {

	final private InvoiceRepository invoiceRepository;

	InvoiceApi(InvoiceRepository invoiceRepository) {
		this.invoiceRepository = invoiceRepository;
	}


	@GetMapping("/{id}")
	public ResponseEntity<Invoice> get(@PathVariable int id) {
		return invoiceRepository.findById(id).map(ResponseEntity::ok).orElseGet((() -> ResponseEntity.notFound().build()));
	}
}
