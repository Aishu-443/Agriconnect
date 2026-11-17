-- ====================================================================
-- AgriConnect Seed Data
-- ====================================================================

-- 1. Admin Farmer
INSERT INTO farmers (id, name, email, mobile, password, state, district, village, farm_size, primary_crop, role, created_at)
VALUES (
    1,
    'Admin',
    'admin@agriconnect.com',
    '9876543210',
    '$2a$10$wE35T49Y7qG9qgP8k1zSkeuGg37D06Qh1pT8m8oJb5a1Jk4qY/aO.',
    'Telangana',
    'Hyderabad',
    'Center',
    0.00,
    'General',
    'ADMIN',
    NOW()
) ON CONFLICT (id) DO NOTHING;

-- 2. Crops (20 Crops across FOOD, COMMERCIAL, PULSES, OILSEEDS, HORTICULTURAL)

-- FOOD CROPS
INSERT INTO crops (id, crop_name, category, season, soil_type, water_requirement, temperature_range, cultivation_method, harvesting_period, description)
VALUES (
    1, 'Rice (Paddy)', 'FOOD', 'Kharif', 'Clayey loam, alluvial', 'High (1000-1500 mm)', '22°C - 32°C',
    'Transplanting / Direct Seeding', '110-140 days',
    'Paddy is the primary staple food crop in India, cultivated extensively in river basins and irrigated plains.'
) ON CONFLICT (id) DO NOTHING;

INSERT INTO crops (id, crop_name, category, season, soil_type, water_requirement, temperature_range, cultivation_method, harvesting_period, description)
VALUES (
    2, 'Wheat', 'FOOD', 'Rabi', 'Well-drained fertile loamy, clay loam', 'Moderate (450-650 mm)', '15°C - 25°C',
    'Broadcasting / Seed Drill', '120-150 days',
    'Wheat is the key winter cereal crop grown predominantly in the Indo-Gangetic plains of North India.'
) ON CONFLICT (id) DO NOTHING;

INSERT INTO crops (id, crop_name, category, season, soil_type, water_requirement, temperature_range, cultivation_method, harvesting_period, description)
VALUES (
    3, 'Maize (Corn)', 'FOOD', 'Kharif / Rabi', 'Sandy loam to silty clay loam', 'Moderate (500-750 mm)', '21°C - 27°C',
    'Line sowing with furrow opener', '90-110 days',
    'Maize serves as food, fodder, and raw material for industrial starch and poultry feed industries.'
) ON CONFLICT (id) DO NOTHING;

INSERT INTO crops (id, crop_name, category, season, soil_type, water_requirement, temperature_range, cultivation_method, harvesting_period, description)
VALUES (
    4, 'Pearl Millet (Bajra)', 'FOOD', 'Kharif', 'Sandy loam, light soils', 'Low (350-500 mm)', '25°C - 35°C',
    'Direct broadcasting or dibbling', '75-90 days',
    'Highly drought-resistant nutri-cereal suitable for semi-arid rainfed regions of western and southern India.'
) ON CONFLICT (id) DO NOTHING;

-- COMMERCIAL CROPS
INSERT INTO crops (id, crop_name, category, season, soil_type, water_requirement, temperature_range, cultivation_method, harvesting_period, description)
VALUES (
    5, 'Cotton', 'COMMERCIAL', 'Kharif', 'Deep black soil (Regur)', 'Moderate (600-800 mm)', '21°C - 30°C',
    'Ridge and furrow dibbling', '150-180 days',
    'Cotton (White Gold) is a leading commercial cash crop powering Indias massive textile industry.'
) ON CONFLICT (id) DO NOTHING;

INSERT INTO crops (id, crop_name, category, season, soil_type, water_requirement, temperature_range, cultivation_method, harvesting_period, description)
VALUES (
    6, 'Sugarcane', 'COMMERCIAL', 'Perennial / Annual', 'Deep rich loamy soils', 'High (1500-2500 mm)', '20°C - 35°C',
    'Setts planting in furrows or trenches', '10-14 months',
    'Major cash crop used for sugar, jaggery, ethanol, and molasses production.'
) ON CONFLICT (id) DO NOTHING;

INSERT INTO crops (id, crop_name, category, season, soil_type, water_requirement, temperature_range, cultivation_method, harvesting_period, description)
VALUES (
    7, 'Jute', 'COMMERCIAL', 'Kharif', 'Alluvial sandy loam', 'High (1200-1500 mm)', '24°C - 35°C',
    'Broadcasting / Line drilling', '120-135 days',
    'Golden fibre crop cultivated predominantly in eastern India for packaging and eco-friendly textiles.'
) ON CONFLICT (id) DO NOTHING;

INSERT INTO crops (id, crop_name, category, season, soil_type, water_requirement, temperature_range, cultivation_method, harvesting_period, description)
VALUES (
    8, 'Tobacco', 'COMMERCIAL', 'Rabi', 'Light sandy or red sandy loams', 'Low to Moderate (400-500 mm)', '20°C - 30°C',
    'Nursery raising followed by field transplanting', '90-120 days',
    'Commercial leaf crop grown primarily in Andhra Pradesh, Gujarat, and Karnataka.'
) ON CONFLICT (id) DO NOTHING;

-- PULSES
INSERT INTO crops (id, crop_name, category, season, soil_type, water_requirement, temperature_range, cultivation_method, harvesting_period, description)
VALUES (
    9, 'Chickpea (Gram/Chana)', 'PULSES', 'Rabi', 'Well-drained medium to heavy soils', 'Low (300-400 mm)', '15°C - 25°C',
    'Drilling / Sowing in rows', '90-120 days',
    'Premier pulse crop of India rich in protein, fixing atmospheric nitrogen and improving soil fertility.'
) ON CONFLICT (id) DO NOTHING;

INSERT INTO crops (id, crop_name, category, season, soil_type, water_requirement, temperature_range, cultivation_method, harvesting_period, description)
VALUES (
    10, 'Pigeon Pea (Tur/Arhar)', 'PULSES', 'Kharif', 'Deep loamy soils with good drainage', 'Moderate (600-650 mm)', '20°C - 30°C',
    'Ridge sowing / Intercropping with cotton or cereals', '150-180 days',
    'Essential protein source widely consumed as dal across India, cultivated extensively in drylands.'
) ON CONFLICT (id) DO NOTHING;

INSERT INTO crops (id, crop_name, category, season, soil_type, water_requirement, temperature_range, cultivation_method, harvesting_period, description)
VALUES (
    11, 'Green Gram (Moong)', 'PULSES', 'Kharif / Summer', 'Well-drained sandy loam', 'Low (350-450 mm)', '25°C - 35°C',
    'Broadcasting / Seed drill line sowing', '60-75 days',
    'Short-duration drought-tolerant legume crop ideal for crop rotation and green manuring.'
) ON CONFLICT (id) DO NOTHING;

INSERT INTO crops (id, crop_name, category, season, soil_type, water_requirement, temperature_range, cultivation_method, harvesting_period, description)
VALUES (
    12, 'Black Gram (Urad)', 'PULSES', 'Kharif / Rabi', 'Medium black to heavy loams', 'Low to Moderate (400-500 mm)', '25°C - 35°C',
    'Line sowing behind plough', '70-90 days',
    'High protein pulse crop essential for traditional South Indian cuisines and soil nitrogen replenishment.'
) ON CONFLICT (id) DO NOTHING;

-- OILSEEDS
INSERT INTO crops (id, crop_name, category, season, soil_type, water_requirement, temperature_range, cultivation_method, harvesting_period, description)
VALUES (
    13, 'Mustard (Sarson)', 'OILSEEDS', 'Rabi', 'Light to heavy loam soils', 'Low (250-400 mm)', '15°C - 25°C',
    'Line sowing in shallow furrows', '100-130 days',
    'Primary edible oilseed crop of Northern and Central India, known for pungent oil and seed cakes.'
) ON CONFLICT (id) DO NOTHING;

INSERT INTO crops (id, crop_name, category, season, soil_type, water_requirement, temperature_range, cultivation_method, harvesting_period, description)
VALUES (
    14, 'Soybean', 'OILSEEDS', 'Kharif', 'Well-drained black clay soils', 'Moderate (550-700 mm)', '22°C - 30°C',
    'Broad bed furrow / Seed drill sowing', '90-105 days',
    'Major dual-purpose oilseed and protein crop cultivated heavily in Madhya Pradesh and Maharashtra.'
) ON CONFLICT (id) DO NOTHING;

INSERT INTO crops (id, crop_name, category, season, soil_type, water_requirement, temperature_range, cultivation_method, harvesting_period, description)
VALUES (
    15, 'Groundnut (Peanut)', 'OILSEEDS', 'Kharif / Rabi', 'Well-drained sandy loam or red sandy soils', 'Moderate (500-600 mm)', '22°C - 30°C',
    'Dibbling / Seed drill with earthing-up', '105-125 days',
    'Major oilseed grown underground; rich in digestible proteins, healthy fats, and vitamins.'
) ON CONFLICT (id) DO NOTHING;

INSERT INTO crops (id, crop_name, category, season, soil_type, water_requirement, temperature_range, cultivation_method, harvesting_period, description)
VALUES (
    16, 'Sunflower', 'OILSEEDS', 'Kharif / Rabi / Zaid', 'Loam, black soils with good moisture retention', 'Low to Moderate (400-550 mm)', '20°C - 30°C',
    'Furrow sowing with optimal plant spacing', '85-100 days',
    'Photo-insensitive crop producing premium cooking oil high in polyunsaturated fatty acids.'
) ON CONFLICT (id) DO NOTHING;

-- HORTICULTURAL CROPS
INSERT INTO crops (id, crop_name, category, season, soil_type, water_requirement, temperature_range, cultivation_method, harvesting_period, description)
VALUES (
    17, 'Tomato', 'HORTICULTURAL', 'Year-round', 'Well-drained loamy to sandy clay', 'Moderate (500-700 mm)', '18°C - 28°C',
    'Nursery raising followed by bed transplanting and trellising', '70-90 days after transplant',
    'High-value vegetable crop consumed fresh and processed into pastes, purees, and ketchups.'
) ON CONFLICT (id) DO NOTHING;

INSERT INTO crops (id, crop_name, category, season, soil_type, water_requirement, temperature_range, cultivation_method, harvesting_period, description)
VALUES (
    18, 'Onion', 'HORTICULTURAL', 'Rabi / Kharif', 'Deep friable loamy soil rich in organic matter', 'Moderate (450-600 mm)', '15°C - 30°C',
    'Raised bed nursery transplanting', '120-150 days',
    'Indispensable culinary bulb crop with large domestic consumption and substantial export markets.'
) ON CONFLICT (id) DO NOTHING;

INSERT INTO crops (id, crop_name, category, season, soil_type, water_requirement, temperature_range, cultivation_method, harvesting_period, description)
VALUES (
    19, 'Potato', 'HORTICULTURAL', 'Rabi', 'Sandy loam rich in organic matter', 'Moderate (500-650 mm)', '15°C - 22°C',
    'Tuber planting on ridges with earthing up', '90-110 days',
    'Most popular tuber vegetable crop, contributing significantly to food security across India.'
) ON CONFLICT (id) DO NOTHING;

INSERT INTO crops (id, crop_name, category, season, soil_type, water_requirement, temperature_range, cultivation_method, harvesting_period, description)
VALUES (
    20, 'Mango', 'HORTICULTURAL', 'Perennial (Summer Harvest)', 'Deep alluvial to lateritic well-drained soils', 'Low to Moderate (irrigated in juvenile stage)', '24°C - 35°C',
    'Grafting / High density orchard planting', 'Perennial (Annual flowering Nov-Dec, harvest Mar-Jun)',
    'The King of Fruits, India is the worlds largest producer of mangoes featuring Alphonso, Banganapalli, and Dasheri.'
) ON CONFLICT (id) DO NOTHING;


-- 3. Market Prices (30+ records covering AP, Telangana, Maharashtra, Punjab, Karnataka)

-- ANDHRA PRADESH
INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (1, 1, 'Guntur APMC', 'Guntur', 'Andhra Pradesh', 2180.00, 2350.00, 2260.00, CURRENT_DATE, 'eNAM Guntur') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (2, 5, 'Kurnool Cotton Market', 'Kurnool', 'Andhra Pradesh', 6800.00, 7450.00, 7150.00, CURRENT_DATE, 'Agmarknet AP') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (3, 17, 'Madanapalle Tomato Yard', 'Chittoor', 'Andhra Pradesh', 1400.00, 2100.00, 1850.00, CURRENT_DATE, 'Madanapalle Yard') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (4, 15, 'Anantapur Oilseed Yard', 'Anantapur', 'Andhra Pradesh', 5900.00, 6400.00, 6150.00, CURRENT_DATE, 'Agmarknet AP') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (5, 20, 'Vijayawada Fruit Market', 'Krishna', 'Andhra Pradesh', 4500.00, 6800.00, 5600.00, CURRENT_DATE, 'Vijayawada APMC') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (6, 12, 'Tenali Pulse Mandi', 'Guntur', 'Andhra Pradesh', 7200.00, 7850.00, 7500.00, CURRENT_DATE, 'eNAM Tenali') ON CONFLICT (id) DO NOTHING;

-- TELANGANA
INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (7, 1, 'Warangal Grain Market (Enumamula)', 'Warangal', 'Telangana', 2200.00, 2400.00, 2300.00, CURRENT_DATE, 'eNAM Enumamula') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (8, 5, 'Adilabad Cotton Mandi', 'Adilabad', 'Telangana', 6900.00, 7600.00, 7300.00, CURRENT_DATE, 'CCI Telangana') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (9, 3, 'Nizamabad Agricultural Market', 'Nizamabad', 'Telangana', 1950.00, 2220.00, 2100.00, CURRENT_DATE, 'eNAM Nizamabad') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (10, 10, 'Khammam Pulse Yard', 'Khammam', 'Telangana', 9200.00, 10400.00, 9850.00, CURRENT_DATE, 'Agmarknet TS') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (11, 11, 'Suryapet Green Gram Mandi', 'Suryapet', 'Telangana', 7800.00, 8500.00, 8200.00, CURRENT_DATE, 'eNAM Suryapet') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (12, 18, 'Bowenpally Vegetable Mandi', 'Hyderabad', 'Telangana', 2200.00, 3100.00, 2650.00, CURRENT_DATE, 'Bowenpally Market') ON CONFLICT (id) DO NOTHING;

-- MAHARASHTRA
INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (13, 14, 'Latur Oilseed Hub', 'Latur', 'Maharashtra', 4300.00, 4800.00, 4600.00, CURRENT_DATE, 'eNAM Latur') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (14, 18, 'Lasalgaon Onion Mandi', 'Nashik', 'Maharashtra', 1800.00, 2900.00, 2450.00, CURRENT_DATE, 'Lasalgaon APMC') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (15, 5, 'Nagpur Cotton Yard', 'Nagpur', 'Maharashtra', 7000.00, 7550.00, 7250.00, CURRENT_DATE, 'Agmarknet MH') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (16, 6, 'Kolhapur Cane & Jaggery Market', 'Kolhapur', 'Maharashtra', 3100.00, 3500.00, 3320.00, CURRENT_DATE, 'Kolhapur APMC') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (17, 10, 'Akola Pulse Market', 'Akola', 'Maharashtra', 9100.00, 10200.00, 9700.00, CURRENT_DATE, 'Akola Mandi') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (18, 17, 'Pune Gultekdi Market Yard', 'Pune', 'Maharashtra', 1300.00, 2200.00, 1750.00, CURRENT_DATE, 'Pune APMC') ON CONFLICT (id) DO NOTHING;

-- PUNJAB
INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (19, 2, 'Khanna Grain Market (Asia Largest)', 'Ludhiana', 'Punjab', 2275.00, 2450.00, 2350.00, CURRENT_DATE, 'Khanna APMC') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (20, 1, 'Amritsar Dana Mandi', 'Amritsar', 'Punjab', 2300.00, 2520.00, 2410.00, CURRENT_DATE, 'Punjab Mandi Board') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (21, 13, 'Jalandhar Mustard Yard', 'Jalandhar', 'Punjab', 5100.00, 5650.00, 5400.00, CURRENT_DATE, 'Agmarknet PB') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (22, 19, 'Hoshiarpur Potato Market', 'Hoshiarpur', 'Punjab', 1100.00, 1600.00, 1380.00, CURRENT_DATE, 'Hoshiarpur APMC') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (23, 3, 'Bathinda Corn Mandi', 'Bathinda', 'Punjab', 1980.00, 2180.00, 2090.00, CURRENT_DATE, 'Punjab Mandi Board') ON CONFLICT (id) DO NOTHING;

-- KARNATAKA
INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (24, 16, 'Hubballi Oilseed Market', 'Dharwad', 'Karnataka', 4800.00, 5400.00, 5150.00, CURRENT_DATE, 'eNAM Hubballi') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (25, 9, 'Kalaburagi Chana Mandi', 'Kalaburagi', 'Karnataka', 5800.00, 6450.00, 6200.00, CURRENT_DATE, 'eNAM Kalaburagi') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (26, 17, 'Kolar Tomato Market', 'Kolar', 'Karnataka', 1500.00, 2350.00, 1950.00, CURRENT_DATE, 'Kolar APMC') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (27, 4, 'Belagavi Grain Yard', 'Belagavi', 'Karnataka', 2150.00, 2480.00, 2320.00, CURRENT_DATE, 'Belagavi APMC') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (28, 18, 'Yeshwanthpur APMC Yard', 'Bengaluru Urban', 'Karnataka', 2300.00, 3200.00, 2800.00, CURRENT_DATE, 'eNAM Bengaluru') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (29, 15, 'Davanagere Peanut Mandi', 'Davanagere', 'Karnataka', 6000.00, 6550.00, 6300.00, CURRENT_DATE, 'Davanagere APMC') ON CONFLICT (id) DO NOTHING;

INSERT INTO market_prices (id, crop_id, market_name, district, state, min_price, max_price, modal_price, price_date, source)
VALUES (30, 2, 'Shimoga Cereal Yard', 'Shimoga', 'Karnataka', 2400.00, 2650.00, 2520.00, CURRENT_DATE, 'Shimoga APMC') ON CONFLICT (id) DO NOTHING;


-- 4. Announcements (2 Official National & Regional Advisories)
INSERT INTO announcements (id, title, content, published_date, created_at)
VALUES (
    1,
    'PM-KISAN 17th Installment Disbursed',
    'The 17th installment of the Pradhan Mantri Kisan Samman Nidhi (PM-KISAN) has been successfully released into eligible farmers bank accounts via Direct Benefit Transfer (DBT). Farmers are advised to complete their biometric e-KYC on the official portal or through nearest CSC centers and verify their Aadhaar-linked bank accounts.',
    CURRENT_DATE,
    NOW()
) ON CONFLICT (id) DO NOTHING;

INSERT INTO announcements (id, title, content, published_date, created_at)
VALUES (
    2,
    'Monsoon Advisory & Pest Management Alert',
    'Following recent monsoon showers across southern and western peninsular states, ICAR agricultural scientists advise farmers to inspect Kharif standing crops for fall armyworm in maize and rust fungal spores in soybean. Please ensure adequate field drainage to prevent root rot and apply recommended bio-pesticides (Neem oil 1500 ppm or Bacillus thuringiensis) upon initial infestation symptoms.',
    CURRENT_DATE,
    NOW()
) ON CONFLICT (id) DO NOTHING;

-- Reset primary key sequences to allow new manual inserts via the REST API
SELECT setval(pg_get_serial_sequence('farmers', 'id'), COALESCE((SELECT MAX(id) FROM farmers), 1));
SELECT setval(pg_get_serial_sequence('crops', 'id'), COALESCE((SELECT MAX(id) FROM crops), 1));
SELECT setval(pg_get_serial_sequence('market_prices', 'id'), COALESCE((SELECT MAX(id) FROM market_prices), 1));
SELECT setval(pg_get_serial_sequence('announcements', 'id'), COALESCE((SELECT MAX(id) FROM announcements), 1));
