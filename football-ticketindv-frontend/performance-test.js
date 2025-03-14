// performance-test.js
import lighthouse from 'lighthouse';
import * as chromeLauncher from 'chrome-launcher';
import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

console.log('Starting performance testing setup...');

// HTML template for the report
const htmlTemplate = `
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lighthouse Performance Report</title>
    <style>
        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
            line-height: 1.6;
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            background: #f5f5f5;
        }
        .container {
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .header {
            text-align: center;
            margin-bottom: 30px;
        }
        .score-card {
            display: flex;
            justify-content: space-around;
            flex-wrap: wrap;
            gap: 20px;
            margin-bottom: 30px;
        }
        .metric-card {
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
            min-width: 200px;
            text-align: center;
        }
        .score {
            font-size: 24px;
            font-weight: bold;
            margin: 10px 0;
        }
        .good { color: #0cce6b; }
        .average { color: #ffa400; }
        .poor { color: #ff4e42; }
        .details {
            margin-top: 30px;
        }
        .timestamp {
            text-align: center;
            color: #666;
            margin-top: 20px;
        }
        .metric-group {
            margin: 20px 0;
            padding: 20px;
            background: #f8f9fa;
            border-radius: 8px;
        }
        .metric-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 0;
            border-bottom: 1px solid #eee;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Lighthouse Performance Report</h1>
            <p>{{URL}}</p>
        </div>
        <div class="score-card">
            {{SCORE_CARDS}}
        </div>
        <div class="details">
            <div class="metric-group">
                <h2>Core Web Vitals</h2>
                {{CORE_WEB_VITALS}}
            </div>
            <div class="metric-group">
                <h2>Other Metrics</h2>
                {{OTHER_METRICS}}
            </div>
        </div>
        <div class="timestamp">
            Generated on {{TIMESTAMP}}
        </div>
    </div>
</body>
</html>
`;

const getScoreClass = (score) => {
    if (score >= 90) return 'good';
    if (score >= 50) return 'average';
    return 'poor';
};

const lighthouseConfig = {
    extends: 'lighthouse:default',
    settings: {
        onlyCategories: ['performance', 'accessibility', 'best-practices', 'seo'],
        formFactor: 'desktop',
        screenEmulation: {
            mobile: false,
            width: 1350,
            height: 940,
            deviceScaleFactor: 1,
            disabled: false,
        },
        throttling: {
            rttMs: 40,
            throughputKbps: 10240,
            cpuSlowdownMultiplier: 1,
        },
    },
};

async function runPerformanceTest(url = 'http://localhost:5173') {
    console.log(`\nTesting ${url}...`);
    
    const chrome = await chromeLauncher.launch({
        chromeFlags: ['--headless', '--disable-gpu', '--no-sandbox']
    });

    try {
        console.log('Running Lighthouse tests...');
        const results = await lighthouse(url, {
            port: chrome.port,
            output: 'html',
            logLevel: 'info',
        }, lighthouseConfig);

        // Create results directory
        const resultsDir = path.join(__dirname, 'performance-results');
        if (!fs.existsSync(resultsDir)) {
            fs.mkdirSync(resultsDir);
        }

        // Generate score cards HTML
        const categories = results.lhr.categories;
        const scoreCards = Object.entries(categories).map(([key, category]) => {
            const score = Math.round(category.score * 100);
            const scoreClass = getScoreClass(score);
            return `
                <div class="metric-card">
                    <h3>${category.title}</h3>
                    <div class="score ${scoreClass}">${score}%</div>
                </div>
            `;
        }).join('');

        // Generate Core Web Vitals HTML
        const coreWebVitals = [
            ['First Contentful Paint', 'first-contentful-paint'],
            ['Largest Contentful Paint', 'largest-contentful-paint'],
            ['Time to Interactive', 'interactive'],
            ['Total Blocking Time', 'total-blocking-time'],
            ['Cumulative Layout Shift', 'cumulative-layout-shift']
        ].map(([title, audit]) => `
            <div class="metric-row">
                <strong>${title}</strong>
                <span>${results.lhr.audits[audit].displayValue}</span>
            </div>
        `).join('');

        // Generate Other Metrics HTML
        const otherMetrics = [
            ['Speed Index', 'speed-index'],
            ['Server Response Time', 'server-response-time']
        ].map(([title, audit]) => `
            <div class="metric-row">
                <strong>${title}</strong>
                <span>${results.lhr.audits[audit].displayValue}</span>
            </div>
        `).join('');

        // Generate HTML report
        let htmlReport = htmlTemplate
            .replace('{{URL}}', url)
            .replace('{{SCORE_CARDS}}', scoreCards)
            .replace('{{CORE_WEB_VITALS}}', coreWebVitals)
            .replace('{{OTHER_METRICS}}', otherMetrics)
            .replace('{{TIMESTAMP}}', new Date().toLocaleString());

        // Save HTML report
        const timestamp = new Date().toISOString().replace(/[:.]/g, '-');
        const htmlReportPath = path.join(resultsDir, `performance-report-${timestamp}.html`);
        fs.writeFileSync(htmlReportPath, htmlReport);

        // Also save JSON for reference
        const jsonReportPath = path.join(resultsDir, `performance-report-${timestamp}.json`);
        fs.writeFileSync(jsonReportPath, JSON.stringify(results.lhr, null, 2));

        console.log(`\nReports generated:`);
        console.log(`HTML Report: ${htmlReportPath}`);
        console.log(`JSON Report: ${jsonReportPath}`);
        console.log('\nOpen the HTML report in your browser to view the results.');

    } catch (error) {
        console.error('Error running performance test:', error.message);
    } finally {
        await chrome.kill();
    }
}

// Run the test
console.log('Performance test starting...');
runPerformanceTest().then(() => {
    console.log('\nPerformance testing complete!');
}).catch(error => {
    console.error('Error during performance test:', error);
});