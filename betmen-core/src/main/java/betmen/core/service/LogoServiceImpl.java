package betmen.core.service;

import betmen.core.entity.Category;
import betmen.core.entity.Cup;
import betmen.core.entity.Team;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.plexus.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class LogoServiceImpl implements LogoService {

    @Autowired
    private SystemVarsService systemVarsService;

    @Override
    public void deleteLogosDir() throws IOException {
        FileUtils.deleteDirectory(systemVarsService.getLogosPath());
    }

    @Override
    public void createLogosDir() throws IOException {
        FileUtils.mkdir(systemVarsService.getLogosPath());
    }

    @Override
    public void uploadLogo(final Category category, final File file) throws IOException {
        FileUtils.copyFile(file, getLogoFile(category));
    }

    @Override
    public void uploadLogo(final Cup cup, final File file) throws IOException {
        FileUtils.copyFile(file, getLogoFile(cup));
    }

    @Override
    public void uploadLogo(final Team team, final File file) throws IOException {
        FileUtils.copyFile(file, getLogoFile(team));
    }

    @Override
    public void deleteLogo(final Category category) throws IOException {
        deleteIfExists(getLogoFile(category));
    }

    @Override
    public void deleteLogo(final Cup cup) throws IOException {
        deleteIfExists(getLogoFile(cup));
    }

    @Override
    public void deleteLogo(final Team team) throws IOException {
        deleteIfExists(getLogoFile(team));
    }

    @Override
    public File getLogoFile(final Category category) throws IOException {

        final File categoryLogosDir = getCategoryLogosDir(category);

        assertDirExists(categoryLogosDir);

        final String logoFileName = category.getLogoFileName();

        if (StringUtils.isNotEmpty(logoFileName)) {
            return new File(categoryLogosDir, logoFileName);
        }

        return null;
    }

    @Override
    public File getLogoFile(final Cup cup) throws IOException {

        final File categoryLogosDir = getCategoryLogosDir(cup.getCategory());

        assertDirExists(categoryLogosDir);

        final String logoFileName = cup.getLogoFileName();

        if (StringUtils.isNotEmpty(logoFileName)) {
            return new File(categoryLogosDir, logoFileName);
        }

        return null;
    }

    @Override
    public File getLogoFile(final Team team) throws IOException {

        final File categoryLogosDir = getCategoryLogosDir(team.getCategory());

        assertDirExists(categoryLogosDir);

        final String logoFileName = team.getLogoFileName();

        if (StringUtils.isNotEmpty(logoFileName)) {
            return new File(categoryLogosDir, logoFileName);
        }

        return null;
    }

    @Override
    public String getLogoURL(final Category category) {
        return String.format("/resources/categories/%d/logo/", category.getId());
    }

    @Override
    public String getLogoURL(final Cup cup) {
        return String.format("/resources/cups/%d/logo/", cup.getId());
    }

    @Override
    public String getLogoURL(final Team team) {
        return String.format("/resources/teams/%d/logo/", team.getId());
    }

    private File getCategoryLogosDir(final Category category) {
        return new File(systemVarsService.getLogosPath(), String.valueOf(category.getId()));
    }

    private void deleteIfExists(final File logoFile) {
        if (logoFile != null && logoFile.exists()) {
            FileUtils.fileDelete(logoFile.getPath());
        }
    }

    private void assertDirExists(final File categoryDir) throws IOException {
        if (!categoryDir.exists()) {
            FileUtils.mkdir(categoryDir.getCanonicalPath());
        }
    }
}
