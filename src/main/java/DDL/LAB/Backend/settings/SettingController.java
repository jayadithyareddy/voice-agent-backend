package DDL.LAB.Backend.settings;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "http://localhost:5173")
public class SettingController {

    private final SettingRepository settingRepository;

    public SettingController(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @GetMapping
    public List<Setting> getSettings() {
        return settingRepository.findAll();
    }

    @GetMapping("/{key}")
    public Setting getSetting(@PathVariable String key) {
        return settingRepository.findBySettingKey(key).orElse(null);
    }

    @PostMapping
    public Setting createSetting(@RequestBody Setting setting) {
        return settingRepository.save(setting);
    }

    @PutMapping("/{key}")
    public Setting updateSetting(
            @PathVariable String key,
            @RequestBody Setting setting
    ) {

        Setting existing =
                settingRepository.findBySettingKey(key).orElse(null);

        if (existing == null) {
            setting.setSettingKey(key);
            return settingRepository.save(setting);
        }

        existing.setSettingValue(setting.getSettingValue());

        return settingRepository.save(existing);
    }

    @DeleteMapping("/{key}")
    public void deleteSetting(@PathVariable String key) {

        Setting existing =
                settingRepository.findBySettingKey(key).orElse(null);

        if (existing != null) {
            settingRepository.delete(existing);
        }
    }
}