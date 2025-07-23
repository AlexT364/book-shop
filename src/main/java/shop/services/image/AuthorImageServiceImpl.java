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
public class AuthorImageServiceImpl implements AuthorImageService{
	
	@Value("${authors.images.folder.path}")
	private String authorsImagesFolderPath;
	@Value("${authors.images.missing.path}")
	private String missingAuthorImage;
	
	@Override
	public Resource getImage(long authorId) {
		Path imagePath = Paths.get(authorsImagesFolderPath).resolve(authorId + ".jpg");
		Resource imageResource = new FileSystemResource(imagePath);
		if(imageResource.exists()) {
			return imageResource;
		}
		
		return new ClassPathResource(missingAuthorImage);
	}
	
	@Override
	public void deleteImage(long authorId) {
		Path imagePath = Paths.get(authorsImagesFolderPath).resolve(authorId + ".jpg");
		try {
			Files.deleteIfExists(imagePath);
		} catch (IOException e) {
			log.warn("Could not delete author's image with id = " + authorId, e);
		}
	}

	@Override
	public void saveImage(long authorId, byte[] image) {
		
		if(!resolveAuthorsImagesFolder()) {
			return;
		}

		Path imagePath = Paths.get(this.authorsImagesFolderPath).resolve(authorId + ".jpg");

		try {
			Files.write(
					imagePath, 
					image, 
					StandardOpenOption.TRUNCATE_EXISTING,
					StandardOpenOption.CREATE);
		} catch (IOException e) {
			log.warn("Could not save image for author with id = " + authorId, e);
		}
	}
	
	
	private boolean resolveAuthorsImagesFolder() {
		Path folderPath = Paths.get(authorsImagesFolderPath);
		try {
			Files.createDirectories(folderPath);
		} catch (IOException e) {
			log.warn("Could not create authors image folder: " + folderPath.toAbsolutePath(), e);
			return false;
		}
		return true;
	}
	
}











