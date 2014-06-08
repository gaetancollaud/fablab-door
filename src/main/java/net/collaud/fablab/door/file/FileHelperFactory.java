package net.collaud.fablab.door.file;

import net.collaud.fablab.common.file.AbstractFileHelperFactory;
import net.collaud.fablab.common.file.FileHelper;
import net.collaud.fablab.common.file.FileHelperException;

/**
 *
 * @author gaetan
 */
public class FileHelperFactory extends AbstractFileHelperFactory {
	
	public static final String FILE_CONFIG = "config.properties";
	public static final int ID_CONFIG = 1;

	private static FileHelperFactory instance;

	protected static FileHelperFactory getInstance() {
		if (instance == null) {
			instance = new FileHelperFactory();
		}
		return instance;
	}

	private FileHelperFactory() {
	}
	
	public static FileHelper<ConfigFileHelper> getConfig(){
		return getInstance().get(ID_CONFIG);
	}

	@Override
	protected FileHelper create(Integer key) throws FileHelperException{
		switch(key){
			case ID_CONFIG:
				return createPropertiesFileHelper(FILE_CONFIG);
		}
		throw getUnknowFileHelperException(key);
	}

}
