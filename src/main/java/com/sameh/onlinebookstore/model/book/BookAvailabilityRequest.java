package com.sameh.onlinebookstore.model.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookAvailabilityRequest {
    private boolean available;
}
