package com.example.parking;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.parking.Repository.BranchRepository;
import com.example.parking.Repository.KioskRepository;
import com.example.parking.model.Branch;
import com.example.parking.model.Kiosk;

@Component
public class KioskDataInitializer implements CommandLineRunner {

    private final KioskRepository kioskRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    public KioskDataInitializer(
            KioskRepository kioskRepository,
            BranchRepository branchRepository,
            PasswordEncoder passwordEncoder) {

        this.kioskRepository = kioskRepository;
        this.branchRepository = branchRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        createKiosk(
                "RAMALLAH_ENTRY_01",
                "secretforramallahentryfirst",
                KioskType.ENTRY,
                1
        );

        createKiosk(
                "RAMALLAH_EXIT_01",
                "secretforramallahexitfirst",
                KioskType.EXIT,
                1
        );
        createKiosk(
                "RAMALLAH_ENTRY_02","secretforramallahentrysecond",KioskType.ENTRY,1
        );
        createKiosk(
                "RAMALLAH_EXIT_02","secretforramallahexitsecond",KioskType.EXIT,1
        );
        createKiosk(
                "NABLUS_ENTRY_01","secretfornablusentryfirst",KioskType.ENTRY,2
        );
        createKiosk(
                "NABLUS_EXIT_01","secretfornablusexitfirst",KioskType.EXIT,2
        );
        createKiosk(
                "BETHLEHEM_ENTRY_01","secretforbethlehementryfirst",KioskType.ENTRY,3
        );
        createKiosk(
                "BETHLEHEM_EXIT_01","secretforbethlehemexitfirst",KioskType.EXIT,3
        );
        createKiosk(
                "JERICHO_ENTRY_01","secretforjerichoentryfirst",KioskType.ENTRY,4
        );
        createKiosk(
                "JERICHO_EXIT_01","secretforjerichoexitfirst",KioskType.EXIT,4
        );
        createKiosk(
                "HEBRON_ENTRY_01","secretforhebronentryfirst",KioskType.ENTRY,5
        );
        createKiosk(
                "HEBRON_EXIT_01","secretforhebronexitfirst",KioskType.EXIT,5
        );
        createKiosk(
                "JENIN_ENTRY_01","secretforjeninentryfirst",KioskType.ENTRY,6
        );
        createKiosk(
                "JENIN_EXIT_01","secretforjeninexitfirst",KioskType.EXIT,6
        );
        createKiosk(
               "TULKARM_ENTRY_01","secretfortulkarmentryfirst",KioskType.ENTRY,7
        );
        createKiosk(
               "TULKARM_EXIT_01","secretfortulkarmexitfirst",KioskType.EXIT,7
        );
    }

    private void createKiosk(
            String name,
            String secret,
            KioskType type,
            int branchId) {

        if (kioskRepository.findByName(name).isPresent()) {
            return;
        }

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() ->
                        new RuntimeException("Branch not found"));

        Kiosk kiosk = new Kiosk();

        kiosk.setName(name);
        kiosk.setSecret(passwordEncoder.encode(secret));
        kiosk.setType(type);
        kiosk.setBranch(branch);
        kiosk.setEnabled(true);

        kioskRepository.save(kiosk);
    }
}