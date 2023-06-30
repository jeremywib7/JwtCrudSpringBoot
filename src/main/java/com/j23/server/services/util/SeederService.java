package com.j23.server.services.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.j23.server.models.auth.Permission;
import com.j23.server.models.auth.Role;
import com.j23.server.models.auth.User;
import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.models.customer.customerOrder.CustomerOrder;
import com.j23.server.models.customer.customerOrder.HistoryProductOrder;
import com.j23.server.models.product.Product;
import com.j23.server.repos.auth.AuthRepository;
import com.j23.server.repos.auth.PermissionRepository;
import com.j23.server.repos.auth.RoleRepository;
import com.j23.server.repos.customer.CustomerProfileRepo;
import com.j23.server.repos.customer.customerOrder.CustomerOrderRepository;
import com.j23.server.repos.customer.customerOrder.HistoryProductOrderRepo;
import com.j23.server.services.restaurant.dashboard.TotalSalesProductService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeederService {

    private final PermissionRepository permissionRepository;
    private final CustomerProfileRepo customerProfileRepo;
    private final CustomerOrderRepository customerOrderRepository;
    private final HistoryProductOrderRepo historyProductOrderRepo;
    private final TotalSalesProductService totalSalesProductService;
    private final AuthRepository authRepository;
    private final RoleRepository roleRepository;

    @Value("${env.seed-all}")
    private boolean seedAll;

    @Value("${env.admin-password}")
    private String adminPassword;

    @Value("${env.seed-role}")
    private boolean seedRole;

    @Value("${env.seed-user}")
    private boolean seedUser;

    @Value("${env.seed-permission}")
    private boolean seedPermission;

    @PostConstruct
    private void init() {
//        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // outputs {bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG
        // remember the password that is printed out and use in the next step
//        System.out.println(encoder.encode("lelegoyeng"));
        if (seedAll) {
            seedPermissions();
            seedRoles();
            seedUsers();
            return;
        }

        if (seedRole) {
            seedRoles();
        }

        if (seedUser) {
            seedUsers();
        }

        if (seedPermission) {
            seedPermissions();
        }
    }

    public void seedCustomerInFirebase() throws FirebaseAuthException {
        // create customer john in firebase auth
        UserRecord.CreateRequest fbJohn = new UserRecord.CreateRequest();
        fbJohn.setUid("john");
        fbJohn.setEmail("john@gmail.com");
        fbJohn.setDisplayName("John");
        fbJohn.setPassword("123456");
        UserRecord recordJohn = FirebaseAuth.getInstance().createUser(fbJohn);

        UserRecord.CreateRequest fbMaria = new UserRecord.CreateRequest();
        fbMaria.setUid("maria");
        fbMaria.setEmail("maria@gmail.com");
        fbMaria.setDisplayName("Maria");
        fbMaria.setPassword("123456");
        UserRecord recordMaria = FirebaseAuth.getInstance().createUser(fbMaria);

        UserRecord.CreateRequest fbThommas = new UserRecord.CreateRequest();
        fbThommas.setUid("thomas");
        fbThommas.setEmail("thomas@gmail.com");
        fbThommas.setDisplayName("Thomas");
        fbThommas.setPassword("123456");
        UserRecord recordThomas = FirebaseAuth.getInstance().createUser(fbThommas);
    }

    public void seedOrder() {
        // get product
        Product burger = new Product();
        burger.setId("burger");

        Product satayAyam = new Product();
        satayAyam.setId("satayayam");

        // create customer john in database
        CustomerProfile userJohn = new CustomerProfile();
        userJohn.setId("john");
        userJohn.setUsername("John");
        userJohn.setFirstName("John");
        userJohn.setLastName("Connor");
        userJohn.setEmail("john@gmail.com");
        userJohn.setGender("Male");
        userJohn.setPassword("123456");
        customerProfileRepo.save(userJohn);

        // create customer maria in database
        CustomerProfile userMaria = new CustomerProfile();
        userMaria.setId("maria");
        userMaria.setUsername("Maria");
        userMaria.setFirstName("Maria");
        userMaria.setLastName("Hill");
        userMaria.setEmail("maria@gmail.com");
        userMaria.setGender("Female");
        userMaria.setPassword("123456");
        customerProfileRepo.save(userMaria);

        // create customer thomas in database
        CustomerProfile userThomas = new CustomerProfile();
        userMaria.setId("maria");
        userMaria.setUsername("Maria");
        userMaria.setFirstName("Maria");
        userMaria.setLastName("Hill");
        userMaria.setEmail("maria@gmail.com");
        userMaria.setGender("Female");
        userMaria.setPassword("123456");
        customerProfileRepo.save(userMaria);


        // create customer john order
        List<HistoryProductOrder> historyProductOrderList1 = new ArrayList<>();
        HistoryProductOrder historyProductOrder1 = new HistoryProductOrder();
        historyProductOrder1.setProduct(burger);
        historyProductOrder1.setDiscount(false);
        historyProductOrder1.setName("Burger");
        historyProductOrder1.setQuantity(2);
        historyProductOrder1.setUnitPrice(BigDecimal.valueOf(40000));
        historyProductOrderRepo.save(historyProductOrder1);
        historyProductOrderList1.add(historyProductOrder1);

        historyProductOrderList1.forEach(historyProductOrder -> {
            totalSalesProductService.sumProductProfit(historyProductOrder.getProduct(), historyProductOrder.getUnitPrice().multiply(
                    BigDecimal.valueOf(historyProductOrder.getQuantity())
            ));
        });

        CustomerOrder order1 = new CustomerOrder();
        order1.setCustomerProfile(userJohn);
        order1.setOrderIsActive(false);
        order1.setOrderFinished(LocalDateTime.of(LocalDate.now().minusDays(7), LocalTime.of(11, 5, 30)));
        order1.setHistoryProductOrders(historyProductOrderList1);
        order1.setTotalPaid(BigDecimal.valueOf(100000));
        order1.setTotalChange(BigDecimal.valueOf(20000));
        order1.setTotalPrice(BigDecimal.valueOf(80000));
        order1.setNumber(1);
        customerOrderRepository.save(order1);
        //

        // create customer maria order
        List<HistoryProductOrder> historyProductOrderList2 = new ArrayList<>();

        HistoryProductOrder historyProductOrder2 = new HistoryProductOrder();
        historyProductOrder2.setProduct(burger);
        historyProductOrder2.setDiscount(false);
        historyProductOrder2.setName("Burger");
        historyProductOrder2.setQuantity(1);
        historyProductOrder2.setUnitPrice(BigDecimal.valueOf(40000));
        historyProductOrderRepo.save(historyProductOrder2);
        historyProductOrderList2.add(historyProductOrder2);

        HistoryProductOrder historyProductOrder3 = new HistoryProductOrder();
        historyProductOrder3.setProduct(satayAyam);
        historyProductOrder3.setDiscount(false);
        historyProductOrder3.setName("Satay Ayam");
        historyProductOrder3.setQuantity(2);
        historyProductOrder3.setUnitPrice(BigDecimal.valueOf(35000));
        historyProductOrderRepo.save(historyProductOrder3);
        historyProductOrderList2.add(historyProductOrder3);

        historyProductOrderList2.forEach(historyProductOrder -> {
            totalSalesProductService.sumProductProfit(historyProductOrder.getProduct(), historyProductOrder.getUnitPrice().multiply(
                    BigDecimal.valueOf(historyProductOrder.getQuantity())
            ));
        });

        CustomerOrder order2 = new CustomerOrder();
        order2.setCustomerProfile(userMaria);
        order2.setOrderIsActive(false);
        order2.setOrderFinished(LocalDateTime.of(LocalDate.now(), LocalTime.now().minusMinutes(50)));
        order2.setHistoryProductOrders(historyProductOrderList2);
        order2.setTotalPaid(BigDecimal.valueOf(40000));
        order2.setTotalChange(BigDecimal.valueOf(0));
        order2.setTotalPrice(BigDecimal.valueOf(40000));
        order2.setNumber(1);
        customerOrderRepository.save(order2);

//        HistoryProductOrder historyProductOrder3 = new HistoryProductOrder();
//        historyProductOrder3.setProduct(satayAyam);
//        historyProductOrder3.setDiscount(false);
//        historyProductOrder3.setName("Satay Ayam");
//        historyProductOrder3.setQuantity(2);
//        historyProductOrder3.setUnitPrice(BigDecimal.valueOf(35000));
//        historyProductOrderRepo.save(historyProductOrder3);
//        historyProductOrderList2.add(historyProductOrder3);
//
//        CustomerOrder order2 = new CustomerOrder();
//        order2.setCustomerProfile(userMaria);
//        order2.setOrderIsActive(false);
//        order2.setOrderFinished(LocalDateTime.of(LocalDate.now(), LocalTime.now().minusMinutes(50)));
//        order2.setHistoryProductOrders(historyProductOrderList2);
//        order2.setTotalPaid(BigDecimal.valueOf(40000));
//        order2.setTotalChange(BigDecimal.valueOf(0));
//        order2.setTotalPrice(BigDecimal.valueOf(40000));
//        order2.setNumber(1);
//        customerOrderRepository.save(order2);
//
//        historyProductOrderList2.forEach(historyProductOrder -> {
//            totalSalesProductService.sumProductProfit(historyProductOrder.getProduct(), historyProductOrder.getUnitPrice().multiply(
//                    BigDecimal.valueOf(historyProductOrder.getQuantity())
//            ));
//        });
    }

    public List<Permission> seedPermissions() {
        List<Permission> permissions = new ArrayList<>();

        Permission adminReadPermission = new Permission("ADMIN_READ");
        adminReadPermission.setValue("admin:read");
        Permission adminCreatePermission = new Permission("ADMIN_CREATE");
        adminCreatePermission.setValue("admin:create");
        Permission adminDeletePermission = new Permission("ADMIN_DELETE");
        adminDeletePermission.setValue("admin:delete");
        Permission adminUpdatePermission = new Permission("ADMIN_UPDATE");
        adminUpdatePermission.setValue("admin:update");

        permissions.add(adminReadPermission);
        permissions.add(adminCreatePermission);
        permissions.add(adminDeletePermission);
        permissions.add(adminUpdatePermission);

        permissionRepository.saveAll(permissions);
        log.info("Permissions seeded");
        return permissions;
    }

    public List<Role> seedRoles() {
        List<Role> roles = new ArrayList<>();

        // ADMIN ROLE
        Role adminRole = new Role();
        adminRole.setName("Admin");
        Set<Permission> adminPermissions = new HashSet<>();
        adminPermissions.add(new Permission("ADMIN_CREATE"));
        adminPermissions.add(new Permission("ADMIN_DELETE"));
        adminRole.setPermissions(adminPermissions);
        roles.add(adminRole);

        roleRepository.saveAll(roles);
        log.info("Roles seeded");
        return roles;
    }

    public void seedUsers() {
        List<GrantedAuthority> authoritiesToAdd = new ArrayList<>();
//        authoritiesToAdd.add();

        User admin = new User();
        admin.setUsername("admin");
        admin.setUserFirstName("Admin");
        admin.setRole(new Role("Admin"));
        admin.setPassword("1234");
        admin.setActive(true);
//        admin.getAuthorities().add();
        authRepository.save(admin);
        log.info("Users seeded");
    }

}
