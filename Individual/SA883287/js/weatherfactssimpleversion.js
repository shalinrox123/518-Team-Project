var randomStrings = [
    "The sky is blue because of the way sunlight interacts with our atmosphere!",
    "You can see the reflection of the moon in the sky sometimes!",
    "Clear skies don't necessarily indicate a clear day!",
    "A cloud is a large group of tiny water droplets that we can see in the air!",
    "Clouds can contain millions of tons of water!",
    "There are many variations of these 3 main cloud types including stratocumulus, altostratus, altocumulus, cirrostratus and cirrocumulus!",
    "With an average cloud height of 2,500 feet and an average speed of 14mph, it would take two minutes for a raindrop to hit the floor!",
    "An inch of rain can measure around 226,000 lbs in an acre of land. In every minute, one billion tons of rain falls on the earth!",
    "Water vapor turns into clouds when it cools and condenses—that is, turns back into liquid water or ice. Within a cloud, water droplets condense onto one another, causing the droplets to grow. When these water droplets get too heavy to stay suspended in the cloud, they fall to Earth as rain!",
    "Thunder is the sound caused by lightning. Because sound travels much slower than light, you'll always see a flash of lightning before you hear any thunder!",
    "A clap of thunder registers around 120 decibels!",
    "It's impossible to have thunder without a lightning bolt, since thunder is produced from a lightning bolt. However, if you're far enough away from a lightning bolt you'll see the flash, but not hear the thunder!",
    "In 1988, a scientist found two identical snow crystals. They came from a storm in Wisconsin!",
    "Freshly fallen snow absorbs sound waves, giving everything a seemingly hushed, quieter ambience after a flurry. But if the snow then melts and refreezes, the ice can reflect sound waves making sound travel further and clearer!",
    "Snow forms when water vapor in the atmosphere freezes into ice crystals!",
    "Mist often forms when warmer air over water suddenly encounters the cooler surface of land!",
    "Mist is seen where warm, moist air meets sudden cooling, such as in exhaled air in the winter, or when throwing water onto the hot stove of a sauna!",
    "Misty is a character in Pokemon!"
];



//document.getElementById("button1").addEventListener("click", generate);

function generate() {
    var randomDiv = document.getElementById("sams-speech-bubble-text");
    var x = Math.floor((Math.random() * 17) + 1);
    randomIndex = x;//Math.ceil((Math.random() * randomStrings.length - 1));
    newText = randomStrings[randomIndex];
    randomDiv.innerHTML = newText;
}
