package com.jslhrd.yorimichi.domain.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public class SliceResponse<T> {

	private final List<T> items;
	private final Long nextId;
	private final boolean hasNext;
	private final int size;

	public static <T> SliceResponse<T> of(
			List<T> findItems,
			int size,
			Function<T, Long> idGetter
	) {
		final int ps = Math.max(size, 0);
		final boolean hasNext = findItems.size() > ps;
		final List<T> items = List.copyOf(findItems.subList(0, Math.min(ps, findItems.size())));
		final Long nextId = items.isEmpty() ? null : requireId(idGetter.apply(items.getLast()));
		return new SliceResponse<>(items, nextId, hasNext, ps);
	}

	private static Long requireId(Long id) {
		if (id == null) throw new IllegalStateException("idGetter returned null");
		return id;
	}
}