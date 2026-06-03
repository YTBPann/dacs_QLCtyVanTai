document.addEventListener("DOMContentLoaded", function () {
    initDropdownMenus();
});

function initDropdownMenus() {
    document.querySelectorAll("[data-menu-button]").forEach(button => {
        button.addEventListener("click", event => {
            event.stopPropagation();

            const menuId = button.getAttribute("data-menu-button");
            const menu = document.getElementById(menuId);

            document.querySelectorAll(".dropdown-panel").forEach(panel => {
                if (panel !== menu) {
                    panel.classList.remove("show");
                }
            });

            if (menu) {
                menu.classList.toggle("show");
            }
        });
    });

    document.addEventListener("click", event => {
        if (!event.target.closest(".header-menu")) {
            document.querySelectorAll(".dropdown-panel").forEach(panel => {
                panel.classList.remove("show");
            });
        }
    });

    document.addEventListener("keydown", event => {
        if (event.key === "Escape") {
            document.querySelectorAll(".dropdown-panel").forEach(panel => {
                panel.classList.remove("show");
            });
        }
    });
}

function normalizeVehicleMapStatus(vehicle) {
    if (!vehicle) {
        return "unknown";
    }

    if (vehicle.active === false) {
        return "inactive";
    }

    const rawStatus = String(
        vehicle.tripStatus ||
        vehicle.status ||
        vehicle.gpsStatus ||
        vehicle.vehicleStatus ||
        ""
    ).toUpperCase();

    if (rawStatus === "IN_PROGRESS") {
        return "in-progress";
    }

    if (rawStatus === "PLANNED") {
        return "planned";
    }

    if (rawStatus === "COMPLETED") {
        return "completed";
    }

    if (
        rawStatus === "INACTIVE" ||
        rawStatus === "DISABLED" ||
        rawStatus === "OFFLINE"
    ) {
        return "inactive";
    }

    if (
        rawStatus === "ACTIVE" ||
        rawStatus === "ONLINE" ||
        vehicle.active === true
    ) {
        return "active";
    }

    return "unknown";
}

function createVehicleMapIcon(vehicle) {
    const statusClass = "status-" + normalizeVehicleMapStatus(vehicle);

    return L.divIcon({
        className: "vehicle-leaflet-icon",
        html: `
            <div class="vehicle-map-marker ${statusClass}">
                <span>🚚</span>
            </div>
        `,
        iconSize: [34, 34],
        iconAnchor: [17, 17],
        popupAnchor: [0, -18]
    });
}