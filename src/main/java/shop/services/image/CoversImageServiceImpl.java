package shop.services.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CoversImageServiceImpl implements CoversImageService{

	@Value("${book.covers.folder.path}")
	private String coversImagesFolderPath;
	@Value("${book.covers.missing.path}")
	private String missingCoverPath;

	@Override
	public Resource getImage(String isbn) {
		Path imagePath = Paths.get(coversImagesFolderPath).resolve(isbn + ".jpg");
		Resource imageResource = new FileSystemResource(imagePath);
		if(imageResource.exists()) {
			return imageResource;
		}
		return new ClassPathResource(missingCoverPath);
	}
	
	@Override
	public void deleteImage(String isbn){
			Path coverImage = Paths.get(coversImagesFolderPath).resolve(isbn + ".jpg");
			try {
				Files.deleteIfExists(coverImage);
			}catch(IOException e) {
				log.warn("Failed to delete cover image for isbn = {}", isbn, e);
			}
	}
	
	@Override
	public void saveImage(String isbn, byte[] image){
		if(!resolveCoversImagesFolder()) {
			return;
		}
		
		Path imagePath = Paths.get(coversImagesFolderPath).resolve(isbn + ".jpg");
		
		try {
			Files.write(
					imagePath, 
					image,
					StandardOpenOption.TRUNCATE_EXISTING,
					StandardOpenOption.CREATE);
		}catch(IOException e) {
			log.warn("Failed to save cover image for isbn = {}", isbn, e);
		}
	}
	
	private boolean resolveCoversImagesFolder() {
		Path folderPath = Paths.get(coversImagesFolderPath);
		try {
			Files.createDirectories(folderPath);
		} catch (IOException e) {
			log.warn("Could not create covers image folder: {}", folderPath.toAbsolutePath(), e);
			return false;
		}
		return true;
	}
}





















