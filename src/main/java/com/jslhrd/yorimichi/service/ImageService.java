package com.jslhrd.yorimichi.service;

import com.jslhrd.yorimichi.domain.ImageDTO;

import java.util.List;

public interface ImageService {

	public List<ImageDTO> findAll(Long rootId);

	public Long getOrCreateByName(ImageDTO image);

	public long save(ImageDTO image);

	public void update(Long imageId, ImageDTO image);

	public void delete(Long imageId);

	public void addImageToRoot(Long rootId, Long imageId);

	public void removeImageFromRoot(Long rootId, Long imageId);
}
