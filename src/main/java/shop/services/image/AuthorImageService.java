package shop.services.image;

import org.springframework.core.io.Resource;

public interface AuthorImageService {
	
	public Resource getImage(long authorId);

	public void deleteImage(long authorId);

	public void saveImage(long authorId, byte[] image);

}
