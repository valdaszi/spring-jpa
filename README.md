# Spring Data JPA & Data REST

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)


### Kas gali būti svarbu

- __Prisijungimai prie DB nurodomi faile application.properties__:
    
    ```
    spring.datasource.url=jdbc:mysql://localhost:3306/store?useUnicode=yes&characterEncoding=UTF-8&serverTimezone=Europe/Vilnius
    spring.datasource.username=root
    spring.datasource.password=mysql123
    ```
    
- __Darbas su DB vyksta naudojant atitinkamus repositorius__ 

    Tai injekcinami objektai, kurie turi būti aprašyti kaip
    interfeisai išplečiantys interfeisus __CrudRepository__ arba __PagingAndSortingRepository__
    ```
    interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {    
    }
    ```
    
    Tuose servisuose ar kontroleriuose, kur mums reikia prieiti prie šio repositoriaus, 
    reikia jį injekcinti su __@Autowired__ arba per konstruktorių.
    ```
    @RestController
    @RequestMapping("/api/product")
    class ProductApi {
    
    	final private ProductRepository productRepository;
    
    	ProductApi(ProductRepository productRepository) {
    		this.productRepository = productRepository;
    	}
    	
    	...
    }
    ```
    
    Repositoriaus interfeise galima apsirašyti ir daugiau mums reikalingų specifinių metodų.
    Šių metodų veikimas nusakomas pagal jų __pavadinimą (!)__ 
    (daugiau info: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/):
    ```
    interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
    
    	List<Product> findByNameContains(String name);
    
    	List<Product> findByPriceLessThan(BigDecimal price);
    }
    ```
    
- __Jei reikia, kad metode visos operacijos su DB būtų vykdomos vienoje transakcijoje, tai tokį metodą reikia anotuoti
    @Transactional__
    ```
    @Transactional
    @GetMapping("/trans-1")
    public int trans1() {
        ...
    }

    ```
    __@Transactional__ anotacija galima pažymėti ir klasę, tai reikš, kad visi tos klasės metodai yra transakciniai.
    
    - _Pastaba:_ Būtinai reikia nustatyti __spring.jpa.properties.hibernate.dialect__ parametro reikšmę faile 
        __application.properties__, nes pvz. __org.hibernate.dialect.MySQL5Dialect__ nepalaiko transakcijų.
         
    
- __Galima automatiškai pagal repositoriaus metodus generuoti REST metodus__
    
    Tam reikia panaudoti Spring Data REST. O taip pat anotuoti atitinkamus repositorius __@RepositoryRestResource__:
    ```
    @RepositoryRestResource(path = "product")
    interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
    }
    ```
    
    
- __Kaip nurodyti nuo kurio URL prasideda Spring Data REST?__
    
    Paprastai, Spring Data REST veikia nuo pagrindinio aplikacijos URL. 
    Jei to nenorim, tai galime nurodyti faile __application.properties__ 
    parametras __spring.data.rest.basePath__:
    ```
    spring.data.rest.basePath=/rest
    ```
    
    Duomenys grąžinami [HAL](https://en.wikipedia.org/wiki/Hypertext_Application_Language) formate. 
    Taip pat galima pasiskaityti [čia](https://spring.io/understanding/HATEOAS) ir 
    [čia](https://docs.spring.io/spring-data/rest/docs/current/reference/html/) 