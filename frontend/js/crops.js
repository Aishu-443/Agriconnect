/**
 * AgriConnect Crops Library Controller
 * Handles crop catalog fetching, category filtering, search, and farmer favorites
 */

let allCrops = [];
let favoriteCropIds = [];

// Comprehensive default catalog of 15+ Indian crops for robust resilience
const DEFAULT_CROPS = [
  {
    id: 1,
    name: 'Rice (Paddy)',
    category: 'Food Crops',
    season: 'Kharif',
    soilType: 'Deep clayey or alluvial loam',
    waterRequirement: 'High (1200-1400mm)',
    temperature: '22°C - 32°C',
    description: 'The primary staple food of India. Requires high humidity, standing water during early growth, and warm temperatures throughout the vegetative phase.'
  },
  {
    id: 2,
    name: 'Wheat',
    category: 'Food Crops',
    season: 'Rabi',
    soilType: 'Well-drained fertile clayey loam',
    waterRequirement: 'Moderate (450-650mm)',
    temperature: '14°C - 24°C',
    description: 'The second most vital cereal grain in India. Requires a cool growing climate and bright sunshine at harvesting time with fertile loam soil.'
  },
  {
    id: 3,
    name: 'Cotton',
    category: 'Commercial',
    season: 'Kharif',
    soilType: 'Deep black soil (Regur)',
    waterRequirement: 'Moderate (500-750mm)',
    temperature: '21°C - 30°C',
    description: 'Known as "White Gold". Major commercial cash crop requiring 210 frost-free days and bright sunshine during boll maturation.'
  },
  {
    id: 4,
    name: 'Sugarcane',
    category: 'Commercial',
    season: 'Perennial / Annual',
    soilType: 'Rich loamy soil with good drainage',
    waterRequirement: 'Very High (1500-2500mm)',
    temperature: '20°C - 35°C',
    description: 'Long duration industrial cash crop supplying the sugar and bio-ethanol industries. Demands warm, humid climate and ample irrigation.'
  },
  {
    id: 5,
    name: 'Maize (Corn)',
    category: 'Food Crops',
    season: 'Kharif / Rabi',
    soilType: 'Deep fertile loam or red sandy loam',
    waterRequirement: 'Moderate (500-700mm)',
    temperature: '18°C - 27°C',
    description: 'Versatile cereal termed the "Queen of Cereals" due to its high genetic yield potential, utilized for food, poultry feed, and starch manufacturing.'
  },
  {
    id: 6,
    name: 'Soybean',
    category: 'Oilseeds',
    season: 'Kharif',
    soilType: 'Well-drained sandy loam to clay',
    waterRequirement: 'Moderate (600-800mm)',
    temperature: '20°C - 30°C',
    description: 'Miracle crop combining high vegetable protein (40%) and oil content (20%). Enriches soil through biological nitrogen fixation.'
  },
  {
    id: 7,
    name: 'Mustard (Rapeseed)',
    category: 'Oilseeds',
    season: 'Rabi',
    soilType: 'Sandy loam to loamy soil',
    waterRequirement: 'Low (250-400mm)',
    temperature: '15°C - 25°C',
    description: 'Major rabi oilseed crop cultivated extensively across Northern and Western India. Requires cool climate and dry weather during harvesting.'
  },
  {
    id: 8,
    name: 'Groundnut (Peanut)',
    category: 'Oilseeds',
    season: 'Kharif / Summer',
    soilType: 'Well-drained light sandy loam',
    waterRequirement: 'Moderate (500-600mm)',
    temperature: '25°C - 30°C',
    description: 'Important oilseed and food legume requiring light-textured loose friable soil so pegs can effortlessly penetrate underground.'
  },
  {
    id: 9,
    name: 'Gram (Chickpea)',
    category: 'Pulses',
    season: 'Rabi',
    soilType: 'Sandy loam to clay loam',
    waterRequirement: 'Low (250-350mm)',
    temperature: '18°C - 26°C',
    description: 'The premier pulse crop of India. Tolerant to drought and plays a paramount role in sustainable crop rotation systems.'
  },
  {
    id: 10,
    name: 'Pigeon Pea (Arhar/Tur)',
    category: 'Pulses',
    season: 'Kharif',
    soilType: 'Deep well-drained loam',
    waterRequirement: 'Low to Moderate (600-650mm)',
    temperature: '20°C - 32°C',
    description: 'Essential pulse utilized for daily dal preparations. Its deep taproot system makes it drought hardy and prevents soil erosion.'
  },
  {
    id: 11,
    name: 'Tomato',
    category: 'Horticultural',
    season: 'Rabi / Kharif / Summer',
    soilType: 'Rich sandy loam with pH 6.0 - 7.0',
    waterRequirement: 'Moderate (400-600mm)',
    temperature: '18°C - 28°C',
    description: 'High-value vegetable crop demanding balanced moisture, staking, and protection against early blight and whiteflies.'
  },
  {
    id: 12,
    name: 'Potato',
    category: 'Horticultural',
    season: 'Rabi',
    soilType: 'Loose, friable sandy loam with organic humus',
    waterRequirement: 'Moderate (400-500mm)',
    temperature: '15°C - 20°C',
    description: 'Most popular vegetable tuber. Demands cool nights for tuberization and sandy friable beds for uninhibited root expansion.'
  },
  {
    id: 13,
    name: 'Onion',
    category: 'Horticultural',
    season: 'Rabi / Kharif',
    soilType: 'Deep friable loam with good drainage',
    waterRequirement: 'Moderate (350-550mm)',
    temperature: '13°C - 24°C',
    description: 'Pungent commercial bulb essential in Indian cuisine. Requires weed-free early beds and precise irrigation cessation prior to harvest.'
  },
  {
    id: 14,
    name: 'Chilli',
    category: 'Horticultural',
    season: 'Kharif / Rabi',
    soilType: 'Black cotton soil or rich alluvial loam',
    waterRequirement: 'Moderate (450-600mm)',
    temperature: '20°C - 30°C',
    description: 'Major spice crop giving both green fresh pods and dry red chillies. Thrives in warm, dry weather during fruit ripening.'
  },
  {
    id: 15,
    name: 'Tea',
    category: 'Commercial',
    season: 'Perennial',
    soilType: 'Deep, acidic, well-drained loam (pH 4.5-5.5)',
    waterRequirement: 'High (1500-2000mm)',
    temperature: '18°C - 30°C',
    description: 'World-famous beverage plantation crop grown on sloped terrains of Assam, Darjeeling, and Nilgiris with frost-free humid conditions.'
  },
  {
    id: 16,
    name: 'Mango',
    category: 'Horticultural',
    season: 'Perennial / Summer Harvest',
    soilType: 'Deep alluvial or red loamy soil',
    waterRequirement: 'Moderate',
    temperature: '24°C - 32°C',
    description: 'The King of Fruits. Requires frost-free climate, dry dry spell during flowering, and abundant warm sunshine during fruit enlargement.'
  }
];

document.addEventListener('DOMContentLoaded', () => {
  requireAuth();

  const user = getUserFromToken();
  const userGreeting = document.getElementById('userGreeting');
  if (userGreeting && user) {
    userGreeting.innerHTML = `<i class="fa-solid fa-circle-user text-emerald me-1"></i> ${user.name || 'Farmer'}`;
  }

  // Load saved favorites from storage
  loadFavoritesLocal();

  // Attach search and filter events
  setupEventListeners();

  // Check URL params for initial filters
  const params = new URLSearchParams(window.location.search);
  const initialCategory = params.get('category');
  const showFavorites = params.get('filter') === 'favorites';

  if (showFavorites) {
    document.getElementById('filterFavoritesBtn').classList.add('active');
  }

  // Initial load
  fetchCrops(initialCategory || 'ALL');
});

function loadFavoritesLocal() {
  try {
    const raw = localStorage.getItem('agriconnect_favorites');
    favoriteCropIds = raw ? JSON.parse(raw) : [1, 2, 3];
  } catch (e) {
    favoriteCropIds = [1, 2, 3];
  }
}

function setupEventListeners() {
  const searchInput = document.getElementById('cropSearchInput');
  const categoryFilter = document.getElementById('categoryFilter');
  const clearBtn = document.getElementById('clearSearchBtn');
  const favBtn = document.getElementById('filterFavoritesBtn');
  const resetBtn = document.getElementById('resetFiltersBtn');

  // Real-time search
  searchInput.addEventListener('input', () => {
    filterAndRenderCrops();
  });

  // Clear search button
  clearBtn.addEventListener('click', () => {
    searchInput.value = '';
    filterAndRenderCrops();
    searchInput.focus();
  });

  // Category dropdown change
  categoryFilter.addEventListener('change', () => {
    const selected = categoryFilter.value;
    updateQuickTagActive(selected);
    filterAndRenderCrops();
  });

  // Quick tag buttons
  const quickTags = document.querySelectorAll('.quick-tag');
  quickTags.forEach(tag => {
    tag.addEventListener('click', () => {
      const cat = tag.getAttribute('data-cat');
      categoryFilter.value = cat;
      updateQuickTagActive(cat);
      filterAndRenderCrops();
    });
  });

  // Favorites toggle button
  favBtn.addEventListener('click', () => {
    favBtn.classList.toggle('active');
    if (favBtn.classList.contains('active')) {
      favBtn.innerHTML = `<i class="fa-solid fa-heart me-1 text-danger"></i> Showing Saved (${favoriteCropIds.length})`;
    } else {
      favBtn.innerHTML = `<i class="fa-regular fa-heart me-1 text-danger"></i> View Saved Favorites`;
    }
    filterAndRenderCrops();
  });

  // Reset filters button
  if (resetBtn) {
    resetBtn.addEventListener('click', () => {
      searchInput.value = '';
      categoryFilter.value = 'ALL';
      favBtn.classList.remove('active');
      favBtn.innerHTML = `<i class="fa-regular fa-heart me-1 text-danger"></i> View Saved Favorites`;
      updateQuickTagActive('ALL');
      filterAndRenderCrops();
    });
  }
}

function updateQuickTagActive(cat) {
  document.querySelectorAll('.quick-tag').forEach(tag => {
    if (tag.getAttribute('data-cat') === cat) {
      tag.classList.add('active');
      tag.style.background = 'var(--primary)';
      tag.style.color = '#022c22';
    } else {
      tag.classList.remove('active');
      tag.style.background = '';
      tag.style.color = '';
    }
  });
}

/**
 * Fetch crops from backend API with fallback
 */
async function fetchCrops(category = 'ALL') {
  let url = `${API_BASE_URL}/api/crops`;
  if (category && category !== 'ALL') {
    url += `?category=${encodeURIComponent(category)}`;
  }

  try {
    const response = await authFetch(url);
    if (response.ok) {
      const data = await response.json();
      if (Array.isArray(data) && data.length > 0) {
        allCrops = data;
        filterAndRenderCrops();
        return;
      }
    }
  } catch (error) {
    console.warn('Crops API call failed, using validated agronomy dataset:', error);
  }

  // Use default dataset if API unavailable
  allCrops = DEFAULT_CROPS;
  filterAndRenderCrops();
}

/**
 * Filter and render crops in UI grid
 */
function filterAndRenderCrops() {
  const searchVal = document.getElementById('cropSearchInput').value.trim().toLowerCase();
  const selectedCat = document.getElementById('categoryFilter').value;
  const isFavoritesOnly = document.getElementById('filterFavoritesBtn').classList.contains('active');

  let filtered = allCrops.filter(crop => {
    // Search query check
    const matchesSearch = !searchVal || 
      crop.name.toLowerCase().includes(searchVal) ||
      (crop.description && crop.description.toLowerCase().includes(searchVal)) ||
      (crop.season && crop.season.toLowerCase().includes(searchVal)) ||
      (crop.soilType && crop.soilType.toLowerCase().includes(searchVal));

    // Category filter check
    const matchesCat = (selectedCat === 'ALL') || (crop.category === selectedCat);

    // Favorites only check
    const matchesFav = !isFavoritesOnly || favoriteCropIds.includes(crop.id);

    return matchesSearch && matchesCat && matchesFav;
  });

  renderCropGrid(filtered);
}

function renderCropGrid(crops) {
  const grid = document.getElementById('cropsGrid');
  const countText = document.getElementById('cropCountText');
  const emptyState = document.getElementById('noResultsState');

  if (crops.length === 0) {
    grid.innerHTML = '';
    emptyState.classList.remove('d-none');
    countText.innerText = '0 crops found';
    return;
  }

  emptyState.classList.add('d-none');
  countText.innerText = `Displaying ${crops.length} scientific crop blueprint${crops.length > 1 ? 's' : ''}`;

  const categoryIcons = {
    'Food Crops': 'fa-wheat-awn',
    'Commercial': 'fa-sack-dollar',
    'Pulses': 'fa-bowl-rice',
    'Oilseeds': 'fa-droplet',
    'Horticultural': 'fa-apple-whole'
  };

  const categoryBadgeColors = {
    'Food Crops': 'badge-emerald',
    'Commercial': 'badge-gold',
    'Pulses': 'badge-purple',
    'Oilseeds': 'badge-sky',
    'Horticultural': 'badge-rose'
  };

  grid.innerHTML = crops.map(crop => {
    const isFav = favoriteCropIds.includes(crop.id);
    const iconClass = categoryIcons[crop.category] || 'fa-seedling';
    const badgeColor = categoryBadgeColors[crop.category] || 'badge-emerald';

    return `
      <div class="col-md-6 col-lg-4 col-xl-3 animate-fade-in">
        <div class="crop-card">
          <!-- Crop Banner Placeholder -->
          <div class="crop-banner">
            <i class="fa-solid ${iconClass} crop-banner-icon"></i>
            <span class="badge ${badgeColor} position-absolute top-0 end-0 m-3">
              ${crop.category}
            </span>
            <span class="badge badge-slate position-absolute bottom-0 start-0 m-3" style="font-size: 0.72rem;">
              <i class="fa-regular fa-calendar-days me-1"></i> ${crop.season || 'All Season'}
            </span>
          </div>

          <!-- Crop Body -->
          <div class="crop-body">
            <h5 class="crop-title">${crop.name}</h5>
            <p class="crop-desc">
              ${crop.description || 'Verified agronomic playbook including soil management, optimal temperature bounds, and irrigation intervals.'}
            </p>

            <div class="glass-panel p-2 mb-3 small">
              <div class="d-flex justify-content-between text-muted mb-1">
                <span><i class="fa-solid fa-water text-sky me-1"></i> Water:</span>
                <span class="text-white">${crop.waterRequirement || 'Moderate'}</span>
              </div>
              <div class="d-flex justify-content-between text-muted">
                <span><i class="fa-solid fa-temperature-half text-warning me-1"></i> Temp:</span>
                <span class="text-white">${crop.temperature || '20°C - 30°C'}</span>
              </div>
            </div>

            <!-- Crop Footer -->
            <div class="crop-footer">
              <a href="crop-details.html?id=${crop.id}" class="btn btn-sm btn-emerald py-1 px-3">
                <i class="fa-solid fa-book-open"></i> Full Blueprint
              </a>
              <button class="btn-favorite ${isFav ? 'active' : ''}" onclick="handleCropFavorite(${crop.id}, this)" title="${isFav ? 'Remove from favorites' : 'Save to favorites'}">
                <i class="${isFav ? 'fa-solid fa-heart text-danger' : 'fa-regular fa-heart'}"></i>
              </button>
            </div>
          </div>
        </div>
      </div>
    `;
  }).join('');
}

/**
 * Handle Favorite toggle via API
 */
window.handleCropFavorite = async function(cropId, btnElement) {
  const index = favoriteCropIds.indexOf(cropId);
  const isFav = index !== -1;

  try {
    const method = isFav ? 'DELETE' : 'POST';
    await authFetch(`${API_BASE_URL}/api/farmers/favorites`, {
      method,
      body: JSON.stringify({ cropId })
    });
  } catch (error) {
    console.warn('Favorites API offline, updating local cache:', error);
  }

  if (isFav) {
    favoriteCropIds.splice(index, 1);
    btnElement.classList.remove('active');
    btnElement.innerHTML = `<i class="fa-regular fa-heart"></i>`;
    showNotification('Removed crop from favorites.', 'info');
  } else {
    favoriteCropIds.push(cropId);
    btnElement.classList.add('active');
    btnElement.innerHTML = `<i class="fa-solid fa-heart text-danger"></i>`;
    showNotification('Crop successfully saved to your farm favorites!', 'success');
  }

  localStorage.setItem('agriconnect_favorites', JSON.stringify(favoriteCropIds));
};
