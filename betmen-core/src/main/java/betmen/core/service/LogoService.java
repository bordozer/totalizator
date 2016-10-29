package betmen.core.service;

import betmen.core.entity.Category;
import betmen.core.entity.Cup;
import betmen.core.entity.Team;

import java.io.File;
import java.io.IOException;

public interface LogoService {

    void deleteLogosDir() throws IOException;

    void createLogosDir() throws IOException;


    void uploadLogo(final Category category, final File file) throws IOException;

    void uploadLogo(final Cup cup, final File file) throws IOException;

    void uploadLogo(final Team team, final File file) throws IOException;


    void deleteLogo(final Category category) throws IOException;

    void deleteLogo(final Cup cup) throws IOException;

    void deleteLogo(final Team team) throws IOException;


    File getLogoFile(final Category category) throws IOException;

    File getLogoFile(final Cup cup) throws IOException;

    File getLogoFile(final Team team) throws IOException;


    String getLogoURL(final Category category);

    String getLogoURL(final Cup cup);

    String getLogoURL(final Team team);
}
