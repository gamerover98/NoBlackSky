package it.gamerover.nbs.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import it.gamerover.nbs.NBS;

/**
 * 
 * @author gamerover98
 *
 */
public class FileManager extends YamlConfiguration {

	private File file = null;

	public FileManager(String name, String copyFrom) throws IOException, InvalidConfigurationException {

		NBS.getInstance().getDataFolder().mkdirs();
		this.file = new File(NBS.getInstance().getDataFolder() , name);

		if (!this.file.exists()) {

			this.file.createNewFile();

			FileOutputStream out = new FileOutputStream(this.file);
			InputStream in = getClass().getResourceAsStream(copyFrom);

			byte[] buffer = new byte[1024];
			int length = 0;
			
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			out.flush();
			out.close();
			in.close();

		}

		this.load();

	}

	public File getFile() {
		return this.file;
	}

	public void save() throws IOException {
		super.save(this.file);
	}

	public void load() throws IOException, InvalidConfigurationException {
		super.load(this.file);
	}

}
