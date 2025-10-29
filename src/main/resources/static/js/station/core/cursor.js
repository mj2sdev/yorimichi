// /js/station/core/cursor.js
export class Cursor {
    constructor({nextId = null, hasNext = false, size = 10}) {
        this.nextId = nextId;
        this.hasNext = hasNext;
        this.size = size;
    }

    applySlice(s) {
        if (s.size) this.size = s.size;
        this.nextId = s.nextId;
        this.hasNext = s.hasNext;
    }

    reset() {
        this.nextId = null;
        this.hasNext = true;
    }
}
