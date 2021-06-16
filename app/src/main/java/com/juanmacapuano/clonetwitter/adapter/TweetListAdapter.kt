package com.juanmacapuano.clonetwitter.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.juanmacapuano.clonetwitter.R
import com.juanmacapuano.clonetwitter.databinding.FragmentTweetItemBinding
import com.juanmacapuano.clonetwitter.service.UserPreferences
import com.juanmacapuano.clonetwitter.service.data.tweets.Tweet

class TweetListAdapter(private val clickListener: (Tweet) -> Unit, private val context: Context, private val userName: String) :
    RecyclerView.Adapter<TweetListAdapter.TweetListHolder>() {
    private var listTweets = ArrayList<Tweet>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TweetListAdapter.TweetListHolder {
        val binding = FragmentTweetItemBinding.inflate(LayoutInflater.from(parent.context))
        return TweetListHolder(binding)
    }

    override fun onBindViewHolder(holder: TweetListAdapter.TweetListHolder, position: Int) {
        holder.bind(listTweets[position], clickListener, context, userName)
    }

    override fun getItemCount(): Int {
        return listTweets.size
    }

    fun setListTweet(listTweet: List<Tweet>) {
        listTweets.clear()
        listTweets.addAll(listTweet)
        notifyDataSetChanged()
    }

    class TweetListHolder(private val binding: FragmentTweetItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tweet: Tweet, clickListener: (Tweet) -> Unit, context: Context, userName: String) {
            binding.tvUserNameTweet.text = tweet.user.userName
            binding.tvMessageTweet.text = tweet.message
            binding.tvLikeNumberTweet.text = tweet.likes.size.toString()
            binding.ivLikeTweet.setOnClickListener {
                clickListener(tweet)
                Log.d("likeClick: ", tweet.user.toString() + "-" + userName + "-" + tweet.id)
            }
            if(tweet.user.photoUrl != "") {
                Glide.with(context)
                    .load("https://www.minitwitter.com/apiv1/uploads/photos/" +
                            tweet.user.photoUrl)
                    .into(binding.ivUserPhotoTweet)
            }
            for (item in tweet.likes) {
                if (item.userName == userName) {
                    Log.d("like: ", tweet.likes.size.toString() + "-" + tweet.id)
                    Glide.with(context)
                        .load(R.drawable.ic_favorite_24dp)
                        .into(binding.ivLikeTweet)
                    binding.tvLikeNumberTweet.resources.getColor(R.color.purple_500)
                }
            }

            //binding.executePendingBindings()
        }
    }

}