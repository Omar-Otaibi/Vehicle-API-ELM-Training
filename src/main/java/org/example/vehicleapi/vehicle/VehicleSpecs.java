package org.example.vehicleapi.vehicle;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import org.example.vehicleapi.owner.Owner;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class VehicleSpecs {

    public static Specification<Vehicles> filterByFields(String text) {
        if (!StringUtils.hasText(text)) return null;
        String pattern = "%" + text.toLowerCase() + "%";

        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("brand")), pattern),
                cb.like(cb.lower(root.get("model")), pattern),
                cb.like(cb.lower(root.get("vin")), pattern)
        );
    }

    public static Specification<Vehicles> hasYear(Integer year) {
        return (root, query, cb) -> year == null ? null : cb.equal(root.get("year"), year);
    }

    public static Specification<Vehicles> plateContains(String plate) {
        return (root, query, cb) ->
                StringUtils.hasText(plate) ? cb.like(root.get("plate"), "%" + plate + "%") : null;
    }

    public static Specification<Vehicles> hasOwnerName(String name) {
        return (root, query, cb) -> {
            //  Safety Check: If search is empty, don't filter
            if (name == null || name.isBlank()) return null;

            // Join the Tables
            Join<Vehicles, Owner> owner = root.join("owner");

            // get full name (FirstName + " " + LastName)
            Expression<String> fullName = cb.concat(
                    cb.concat(owner.get("firstName"), " "),
                    owner.get("lastName")
            );

            // WHERE LOWER(first_name + ' ' + last_name) LIKE '%Omar%'
            return cb.like(cb.lower(fullName), "%" + name.toLowerCase() + "%");
        };
    }
}
