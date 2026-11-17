
/**
 * AgriConnect Dashboard Controller
 * Fetches farmer profile, metrics, announcements, and featured crops
 */

document.addEventListener('DOMContentLoaded', async () => {
  requireAuth();

  const user = getUserFromToken();

  setupSidebar();
  updateDateDisplay();
  populateBasicUserInfo(user);

  loadFarmerProfile(user);
  loadDashboardWeather();
  loadAnnouncements();
  loadFeaturedCrops();
  loadSavedCropsMetric();
});

/**
 * Configure mobile sidebar toggles
 */
function setupSidebar() {
  const toggleBtn = document.getElementById('toggleSidebarBtn');
  const closeBtn = document.getElementById('closeSidebarBtn');
  const sidebar = document.getElementById('appSidebar');
  const overlay = document.getElementById('sidebarOverlay');

  if (toggleBtn && sidebar && overlay) {
    toggleBtn.addEventListener('click', () => {
      sidebar.classList.add('show');
      overlay.classList.add('show');
    });

    const closeHandler = () => {
      sidebar.classList.remove('show');
      overlay.classList.remove('show');
    };

    if (closeBtn) closeBtn.addEventListener('click', closeHandler);
    overlay.addEventListener('click', closeHandler);
  }

  const user = getUserFromToken();
  const isAdmin = user && (user.role === 'ADMIN' || user.role === 'ROLE_ADMIN');

  const adminSidebarLink = document.getElementById('adminSidebarLink');
  const dropdownAdminLink = document.getElementById('dropdownAdminLink');

  if (!isAdmin) {
    if (adminSidebarLink) adminSidebarLink.style.display = 'none';
    if (dropdownAdminLink) dropdownAdminLink.style.display = 'none';
  }
}

/**
 * Display formatted current date
 */
function updateDateDisplay() {
  const topbarDate = document.getElementById('topbarDate');

  if (topbarDate) {
    const options = {
      weekday: 'long',
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    };

    topbarDate.innerText = new Date().toLocaleDateString('en-IN', options);
  }
}

/**
 * Populate immediate data from decoded token
 */
function populateBasicUserInfo(user) {
  if (!user) return;

  const displayName = user.name || user.sub || 'Farmer';
  const roleName = user.role || 'FARMER';
  const initial = displayName.charAt(0).toUpperCase();

  const navName = document.getElementById('navbarUserName');
  if (navName) navName.innerText = displayName;

  const sideName = document.getElementById('sidebarUserName');
  if (sideName) sideName.innerText = displayName;

  const sideRole = document.getElementById('sidebarUserRole');
  if (sideRole) sideRole.innerText = roleName;

  const sideAvatar = document.getElementById('sidebarUserAvatar');
  if (sideAvatar) sideAvatar.innerText = initial;

  const welcomeTitle = document.getElementById('welcomeFarmerTitle');
  if (welcomeTitle) welcomeTitle.innerText = `Welcome back, ${displayName}!`;

  const modalName = document.getElementById('modalFullName');
  if (modalName) modalName.innerText = displayName;

  const modalEmail = document.getElementById('modalEmail');
  if (modalEmail) modalEmail.innerText = user.email || user.sub || 'N/A';

  const modalAvatar = document.getElementById('modalAvatar');
  if (modalAvatar) modalAvatar.innerText = initial;
}

/**
 * Fetch Farmer Profile from backend API
 */
async function loadFarmerProfile(user) {
  try {
    const response = await authFetch(`${API_BASE_URL}/api/farmers/profile`);

    if (response.ok) {
      const data = await response.json();
      applyProfileData(data);
    } else {
      applyProfileDefaults(user);
    }
  } catch (err) {
    console.warn('Farmer profile API offline, applying session defaults:', err);
    applyProfileDefaults(user);
  }
}

function applyProfileData(data) {
  const loc = `${data.district || 'Visakhapatnam'}, ${data.state || 'Andhra Pradesh'}`;
  const acres = `${data.farmSize || 5} Acres`;
  const crop = data.primaryCrop || 'Rice (Paddy)';

  const locEl = document.getElementById('farmerLocation');
  if (locEl) locEl.innerText = loc;

  const sizeEl = document.getElementById('farmerFarmSize');
  if (sizeEl) sizeEl.innerText = acres;

  const cropEl = document.getElementById('farmerPrimaryCrop');
  if (cropEl) cropEl.innerText = crop;

  const setEl = (id, val) => {
    const el = document.getElementById(id);
    if (el) el.innerText = val || '--';
  };

  setEl('modalMobile', data.mobile || data.phoneNumber);
  setEl('modalState', data.state);
  setEl('modalDistrict', data.district);
  setEl('modalVillage', data.village);
  setEl('modalFarmSize', acres);
  setEl('modalPrimaryCrop', crop);
}

function applyProfileDefaults(user) {
  applyProfileData({
    name: user ? user.name : 'Farmer',
    mobile: '9876543210',
    state: 'Andhra Pradesh',
    district: 'Visakhapatnam',
    village: 'Anandapuram',
    farmSize: 5,
    primaryCrop: 'Rice (Paddy)'
  });
}

/**
 * Mini weather fetch for dashboard widget
 */
async function loadDashboardWeather() {
  const lat = 17.6868;
  const lon = 83.2185;

  try {
    const res = await fetch(
      `${WEATHER_BASE_URL}/weather/current?latitude=${lat}&longitude=${lon}`
    );

    if (res.ok) {
      const data = await res.json();

      const temp = Math.round(data.temperature || data.temp || 29);
      const humidity = data.humidity || 68;
      const cond = data.condition || data.weather_condition || 'Clear Sky';

      const tempEl = document.getElementById('statWeatherTemp');
      if (tempEl) tempEl.innerText = `${temp}°C`;

      const condEl = document.getElementById('statWeatherCond');
      if (condEl) {
        condEl.innerHTML = `<i class="fa-solid fa-sun text-warning me-1"></i> ${cond}`;
      }

      const humEl = document.getElementById('statWeatherHumidity');
      if (humEl) humEl.innerText = `${humidity}% Humidity`;

      const topBadge = document.getElementById('topbarWeatherBadge');
      if (topBadge) topBadge.innerText = `Visakhapatnam, ${temp}°C`;
    }
  } catch (e) {
    console.log('Weather microservice running on defaults');
  }
}

/**
 * Load Announcements from backend
 */
async function loadAnnouncements() {
  const container = document.getElementById('announcementsContainer');
  if (!container) return;

  try {
    const res = await authFetch(`${API_BASE_URL}/api/announcements`);

    if (res.ok) {
      const announcements = await res.json();

      if (Array.isArray(announcements) && announcements.length > 0) {
        renderAnnouncements(announcements.slice(0, 4));
        return;
      }
    }
  } catch (err) {
    console.warn('Announcements API offline, showing sample bulletin feed:', err);
  }

  const sampleAnnouncements = [
    {
      id: 1,
      title: 'PM-KISAN 17th Tranche Disbursement',
      category: 'Subsidy & Scheme',
      date: 'Today, 09:30 AM',
      content: 'Direct benefit transfer of ₹2,000 credited to verified Aadhaar-linked bank accounts for active cultivators.'
    },
    {
      id: 2,
      title: 'Kharif Minimum Support Price (MSP) Raised',
      category: 'Market Policy',
      date: 'Yesterday',
      content: 'Cabinet Committee on Economic Affairs hikes paddy MSP by ₹117 per quintal for the upcoming marketing season.'
    },
    {
      id: 3,
      title: 'Subsidized Solar Drip Irrigation Applications Open',
      category: 'Irrigation',
      date: 'Nov 12, 2026',
      content: 'State Agriculture Dept offering 80% subsidy on micro-drip solar sets. Apply before end of the month.'
    }
  ];

  renderAnnouncements(sampleAnnouncements);
}

function renderAnnouncements(items) {
  const container = document.getElementById('announcementsContainer');
  if (!container) return;

  const badgeEl = document.getElementById('announcementCountBadge');
  if (badgeEl) badgeEl.innerText = `${items.length} Updates`;

  const topAnnounce = items[0];
  const statHeadline = document.getElementById('statAnnounceHeadline');

  if (statHeadline && topAnnounce) {
    statHeadline.innerText = topAnnounce.title;
  }

  container.innerHTML = items.map(item => `
    <div class="glass-panel p-3 border border-emerald border-opacity-10 hover-scale" style="transition: transform 0.2s ease;">
      <div class="d-flex align-items-center justify-content-between mb-1">
        <span class="badge badge-emerald small">${item.category || 'Official Notice'}</span>
        <small class="text-white-50">${item.date || 'Recent'}</small>
      </div>
      <h6 class="text-white fw-bold mb-1 mt-2">${item.title}</h6>
      <p class="text-muted small mb-0">${item.content || item.description || ''}</p>
    </div>
  `).join('');
}

/**
 * Load Featured Crops from backend
 */
async function loadFeaturedCrops() {
  const container = document.getElementById('featuredCropsContainer');
  if (!container) return;

  try {
    const res = await authFetch(`${API_BASE_URL}/api/crops`);

    if (res.ok) {
      const crops = await res.json();

      if (Array.isArray(crops) && crops.length > 0) {
        renderFeaturedCrops(crops.slice(0, 4));
        return;
      }
    }
  } catch (err) {
    console.warn('Crops API offline, showing sample crops:', err);
  }

  const sampleCrops = [
    {
      id: 1,
      name: 'Rice (Paddy)',
      category: 'Food Crops',
      season: 'Kharif',
      soilType: 'Clayey Loam',
      waterRequirement: 'High (1200-1400mm)',
      temperature: '22°C - 32°C',
      description: 'The staple food crop of India. Requires high humidity, standing water during early growth, and fertile alluvial soil.'
    },
    {
      id: 2,
      name: 'Wheat',
      category: 'Food Crops',
      season: 'Rabi',
      soilType: 'Well-drained Loam',
      waterRequirement: 'Moderate (450-650mm)',
      temperature: '15°C - 25°C',
      description: 'Second most vital cereal grain. Thrives in cool growing seasons and bright sunshine at harvesting time.'
    },
    {
      id: 3,
      name: 'Cotton',
      category: 'Commercial',
      season: 'Kharif',
      soilType: 'Black Soil (Regur)',
      waterRequirement: 'Moderate (500-750mm)',
      temperature: '21°C - 30°C',
      description: 'Major fiber cash crop requiring 210 frost-free days and bright sunshine for clean boll opening.'
    },
    {
      id: 4,
      name: 'Chickpea (Gram)',
      category: 'Pulses',
      season: 'Rabi',
      soilType: 'Sandy Loam',
      waterRequirement: 'Low (250-400mm)',
      temperature: '20°C - 28°C',
      description: 'Crucial protein pulse that fixes atmospheric nitrogen, enriching soil fertility with minimal irrigation.'
    }
  ];

  renderFeaturedCrops(sampleCrops);
}

/**
 * Render featured crop cards
 */
function renderFeaturedCrops(crops) {
  const container = document.getElementById('featuredCropsContainer');
  if (!container) return;

  const iconMap = {
    'Food Crops': 'fa-wheat-awn',
    'Commercial': 'fa-sack-dollar',
    'Pulses': 'fa-bowl-rice',
    'Oilseeds': 'fa-droplet',
    'Horticultural': 'fa-apple-whole'
  };

  container.innerHTML = crops.map(crop => {
    const icon = iconMap[crop.category] || 'fa-seedling';
    const cropName = crop.name || crop.cropName || 'Unknown crop';
    const category = crop.category || 'Other';
    const description = crop.description || 'Crop information is not available.';

    return `
      <div class="col-sm-6">
        <div class="crop-card h-100 p-3">
          <div class="d-flex align-items-center justify-content-between mb-2">
            <span class="badge badge-emerald">${category}</span>
            <span class="badge badge-gold">${crop.season || 'All Season'}</span>
          </div>

          <div class="d-flex align-items-center gap-3 my-2">
            <div class="stat-icon" style="background: rgba(16, 185, 129, 0.15); color: var(--primary-light);">
              <i class="fa-solid ${icon}"></i>
            </div>
            <div>
              <h6 class="text-white fw-bold mb-0">${cropName}</h6>
              <small class="text-muted">
                <i class="fa-solid fa-cloud-rain me-1"></i>
                ${crop.waterRequirement || 'Moderate'}
              </small>
            </div>
          </div>

          <p class="text-muted small mb-3 flex-grow-1" style="display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">
            ${description}
          </p>

          <div class="d-flex justify-content-between align-items-center pt-2 border-top border-white border-opacity-10">
            <a href="crop-details.html?id=${crop.id}" class="btn btn-sm btn-outline-emerald py-1 px-3">
              Full Guide &rarr;
            </a>

            <button class="btn-favorite" onclick="toggleFavorite(${crop.id}, this)" title="Save to favorites">
              <i class="fa-regular fa-heart"></i>
            </button>
          </div>
        </div>
      </div>
    `;
  }).join('');
}

/**
 * Saved Crops Metric
 */
function loadSavedCropsMetric() {
  const saved = JSON.parse(
    localStorage.getItem('agriconnect_favorites') || '["Rice", "Wheat", "Cotton"]'
  );

  const el = document.getElementById('statSavedCrops');
  if (el) el.innerText = saved.length;
}

window.toggleFavorite = async function(cropId, btn) {
  let favorites = JSON.parse(
    localStorage.getItem('agriconnect_favorites') || '[]'
  );

  const idx = favorites.indexOf(cropId);
  const isFav = idx !== -1;

  try {
    await authFetch(`${API_BASE_URL}/api/farmers/favorites`, {
      method: isFav ? 'DELETE' : 'POST',
      body: JSON.stringify({ cropId: cropId })
    });
  } catch (e) {
    // Local fallback
  }

  if (isFav) {
    favorites.splice(idx, 1);
    btn.classList.remove('active');
    btn.innerHTML = `<i class="fa-regular fa-heart"></i>`;
    showNotification('Removed crop from saved favorites.', 'info');
  } else {
    favorites.push(cropId);
    btn.classList.add('active');
    btn.innerHTML = `<i class="fa-solid fa-heart text-danger"></i>`;
    showNotification('Saved crop to your farm favorites!', 'success');
  }

  localStorage.setItem('agriconnect_favorites', JSON.stringify(favorites));
  loadSavedCropsMetric();
};
