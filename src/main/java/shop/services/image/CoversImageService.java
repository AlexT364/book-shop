package shop.services.image;

import org.springframework.core.io.Resource;

public interface CoversImageService {
	
	public Resource getImage(String isbn);

	public void deleteImage(String isbn);

//	public void saveImage(String isbn, Resource image);

	void saveImage(String isbn, byte[] image);

}
