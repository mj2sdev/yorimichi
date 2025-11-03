package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.ImageDTO;
import com.jslhrd.yorimichi.exception.BadRequestException;
import com.jslhrd.yorimichi.exception.ImageNotFoundException;
import com.jslhrd.yorimichi.exception.RootNotFoundException;
import com.jslhrd.yorimichi.mapper.ImageMapper;
import com.jslhrd.yorimichi.mapper.RootImageMapper;
import com.jslhrd.yorimichi.mapper.RootMapper;
import com.jslhrd.yorimichi.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageManager implements ImageService {

	private final ImageMapper imageMapper;
	private final RootImageMapper rootImageMapper;
	private final RootMapper rootMapper;

	@Override
	@Transactional(readOnly = true)
	public List<ImageDTO> findAll(Long rootId) {
		return rootImageMapper.selectByRootId(rootId);
	}

	@Override
	public Long getOrCreateByName(ImageDTO image) {

		String url = image.getUrl();

		Long findImageId = imageMapper.selectIdByUrl(url);
		if (findImageId != null) {
			return findImageId;
		}

		try {
			save(image);
			return image.getId();
		} catch (DuplicateKeyException e) {
			findImageId = imageMapper.selectIdByUrl(url);
			if (findImageId != null) {
				return findImageId;
			}

			throw e;
		}
	}

	@Override
	public long save(ImageDTO image) {

		imageMapper.insert(image);
		log.info("Image: created imageId={}", image.getId());
		return image.getId();
	}

	@Override
	public void update(Long imageId, ImageDTO image) {

		if (image.getId() != null && !imageId.equals(image.getId())) {
			throw new BadRequestException("경로의 imageId 와 본문의 id 가 다릅니다.");
		}

		boolean affected = imageMapper.update(imageId, image) > 0;
		if (!affected) {
			assertExistImage(imageId);
			log.debug("Image: update no-op imageId={}, image={}", imageId, image);
			return;
		}

		log.info("Image: updated imageId={}", imageId);
	}

	@Override
	public void delete(Long imageId) {

		boolean affected = imageMapper.deleteById(imageId) > 0;
		if (!affected) {
			assertExistImage(imageId);
			log.debug("Image: delete no-op imageId={}", imageId);
			return;
		}

		log.info("Image: deleted imageId={}", imageId);
	}

	@Override
	public void addImageToRoot(Long rootId, Long imageId) {

		assertActiveRoot(rootId);
		assertExistImage(imageId);

		try {
			rootImageMapper.insert(rootId, imageId);
		} catch (DuplicateKeyException e) {
			log.debug("RootImage: add no-op rootId={}, imageId={}", rootId, imageId);
			return;
		}

		log.info("RootImage: add rootId={}, imageId={}", rootId, imageId);
	}


	@Override
	public void removeImageFromRoot(Long rootId, Long imageId) {

		boolean affected = rootImageMapper.delete(rootId, imageId) > 0;
		if (!affected) {
			log.debug("RootImage: remove no-op rootId={}, imageId={}", rootId, imageId);
			return;
		}

		log.info("RootImage: removed rootId={}, imageId={}", rootId, imageId);
	}

	private void assertExistImage(Long imageId) {
		boolean exists = imageMapper.existsById(imageId);
		if (!exists) {
			throw new ImageNotFoundException(imageId);
		}
	}

	private void assertActiveRoot(Long rootId) {
		boolean exists = rootMapper.existsActive(rootId);
		if (!exists) {
			throw new RootNotFoundException(rootId);
		}
	}
}